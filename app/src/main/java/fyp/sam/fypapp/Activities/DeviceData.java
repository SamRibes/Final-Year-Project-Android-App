package fyp.sam.fypapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fyp.sam.fypapp.DataManagers.FireBaseInterface;
import fyp.sam.fypapp.DataManagers.LocalDataManager;
import fyp.sam.fypapp.ExpertSystem.ExpertSystem;
import fyp.sam.fypapp.ExpertSystem.InferenceEngine;
import fyp.sam.fypapp.Fragments.AllSensorsFragment;
import fyp.sam.fypapp.Fragments.HumidFragment;
import fyp.sam.fypapp.Fragments.LightFragment;
import fyp.sam.fypapp.Fragments.MoistFragment;
import fyp.sam.fypapp.Fragments.SectionsStatePagerAdapter;
import fyp.sam.fypapp.Fragments.TempFragment;
import fyp.sam.fypapp.R;

public class DeviceData extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private ViewPager mViewPager;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<String> sensorDataList = new ArrayList<>();
    public String[][] dataForGraphs;
    public String[] plant_Limits;
    FileInputStream sensorInputStream, plantInputStream;
    FileOutputStream sensorDataOutputStream, plantLimitsOutputStream;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
            sensorInputStream = openFileInput("Local_Sensor_Data.txt");
            plantInputStream = openFileInput("Local_Plant_Limits.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LocalDataManager.initialisePlantLimitsInputStream(plantInputStream);
        LocalDataManager.initialiseSensorDataInputStream(sensorInputStream);

        dataForGraphs = LocalDataManager.getLocalSensorData();
        plant_Limits = LocalDataManager.getLocalPlant_Limits();

        String suggestions = InferenceEngine.run(ExpertSystem.BuildFactBase(dataForGraphs[0], plant_Limits), ExpertSystem.BuildKnowledgeBases(plant_Limits));

        setContentView(R.layout.data_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SectionsStatePagerAdapter mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containter);

        setupViewPager(mViewPager);

        TextView textView = (TextView) findViewById(R.id.suggestion_box);

        textView.setText(suggestions);
    }

    private void  setupViewPager(ViewPager viewPager)
    {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllSensorsFragment(), "All Sensors Fragment");
        adapter.addFragment(new LightFragment(), "Light Fragment");
        adapter.addFragment(new TempFragment(), "Temperature Fragment");
        adapter.addFragment(new HumidFragment(), "Humid Fragment");
        adapter.addFragment(new MoistFragment(), "Moist Fragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber)
    {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_refresh);
        ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                item.setEnabled(true);
            }
        }
        else
        {
            item.setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_refresh)
        {
            final ProgressDialog progressDialog= ProgressDialog.show(DeviceData.this, "",
                    "Loading. Please wait...", true);
            try
            {
                plantLimitsOutputStream = openFileOutput("Local_Plant_Limits.txt", Context.MODE_PRIVATE);
                sensorDataOutputStream = openFileOutput("Local_Sensor_Data.txt", Context.MODE_PRIVATE);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            FireBaseInterface.initialisePlantLimitsOutputStream(plantLimitsOutputStream);
            FireBaseInterface.getDataFromServer(db, "Plant_Rules");

            FireBaseInterface.initialiseSensorDataOutputStream(sensorDataOutputStream);
            FireBaseInterface.getDataFromServer(db, "SensorData");

            Map<String, Object> data = new HashMap<>();
            data.put("refresh", true);

            final DocumentReference docRef = db.collection("SensorData").document("Refresh");
            docRef.set(data, SetOptions.merge());

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        if(!((boolean) Objects.requireNonNull(snapshot.getData()).get("refresh")))
                        {
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(DeviceData.this, DeviceData.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }, 3000);
                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

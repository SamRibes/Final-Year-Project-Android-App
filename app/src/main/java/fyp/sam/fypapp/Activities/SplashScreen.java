package fyp.sam.fypapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fyp.sam.fypapp.DataManagers.FireBaseInterface;
import fyp.sam.fypapp.R;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FileOutputStream sensorDataOutputStream, plantLimitsOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences("fyp.sam.fypapp", MODE_PRIVATE);

        boolean connected = false;
        ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                createDataFiles();

                FireBaseInterface.initialisePlantLimitsOutputStream(plantLimitsOutputStream);
                FireBaseInterface.getDataFromServer("PlantRules");

                FireBaseInterface.initialiseSensorDataOutputStream(sensorDataOutputStream);
                FireBaseInterface.getDataFromServer("SensorData");
            }
        }
        else
        {
            Toast.makeText(this, "Error getting document. Using local version.", Toast.LENGTH_SHORT).show();
        }

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashScreen.this, DeviceData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }

    public void createDataFiles()
    {
        try
        {
            plantLimitsOutputStream = openFileOutput("Local_Plant_Limits.txt", Context.MODE_PRIVATE);
            sensorDataOutputStream = openFileOutput("Local_Sensor_Data.txt", Context.MODE_PRIVATE);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
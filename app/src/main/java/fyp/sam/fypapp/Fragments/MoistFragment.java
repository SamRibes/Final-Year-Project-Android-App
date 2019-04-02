package fyp.sam.fypapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;

import java.util.Objects;

import fyp.sam.fypapp.Activities.DeviceData;
import fyp.sam.fypapp.R;
import fyp.sam.fypapp.DataManagers.SetUpGraphs;

public class MoistFragment extends Fragment
{
    public String mostRecentValue = null;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.data_moist_fragment, container, false);

        GraphView graph = view.findViewById(R.id.moist_graph_view);

        SetUpGraphs.setUpLineGraph(((DeviceData) Objects.requireNonNull(getActivity())).dataForGraphs, ((DeviceData)getActivity()).plant_Limits,  graph, 'm');

        ImageButton btnNavFragAll = (ImageButton) view.findViewById(R.id.nav_button_all);

        TextView textView = view.findViewById(R.id.current_moist);

        ((DeviceData) getActivity()).db.collection("SensorData")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    {
                        if(document.getId().equals("Latest"))
                        {
                            mostRecentValue = "" +  document.get("moisture");
                        }
                        else
                        {
                            mostRecentValue = ((DeviceData) (Objects.requireNonNull(getActivity()))).dataForGraphs[0][5];
                        }
                    }
                } else {
                    mostRecentValue = ((DeviceData) (Objects.requireNonNull(getActivity()))).dataForGraphs[0][5];
                }
            }
        });

        textView.setText(this.getString(R.string.current_moisture) + " " + mostRecentValue);

        btnNavFragAll.setOnClickListener(new View.OnClickListener()
                                          {
                                              @Override
                                              public void onClick(View view)
                                              {
                                                  Toast.makeText(getActivity(), "Going to All Sensors", Toast.LENGTH_SHORT).show();
                                                  ((DeviceData) Objects.requireNonNull(getActivity())).setViewPager(0);
                                              }
                                          }
        );
        return view;
    }
}


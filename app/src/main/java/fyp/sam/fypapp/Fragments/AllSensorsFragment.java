package fyp.sam.fypapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

import java.util.Objects;

import fyp.sam.fypapp.Activities.DeviceData;
import fyp.sam.fypapp.R;
import fyp.sam.fypapp.DataManagers.SetUpGraphs;

public class AllSensorsFragment
        extends Fragment
{

    @Nullable
    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.data_all_sensors_fragment, container, false);

        GraphView graph = view.findViewById(R.id.all_sensor_graph);

        SetUpGraphs.setUpBarChart(((DeviceData) Objects.requireNonNull(getActivity())).dataForGraphs, ((DeviceData)getActivity()).plant_Limits,  graph);

        ImageButton btnNavFragTemp = (ImageButton) view.findViewById(R.id.nav_button_temp);
        ImageButton btnNavFragHumid = (ImageButton) view.findViewById(R.id.nav_button_humid);
        ImageButton btnNavFragLight = (ImageButton) view.findViewById(R.id.nav_button_light);
        ImageButton btnNavFragMoist = (ImageButton) view.findViewById(R.id.nav_button_moist);

        btnNavFragLight.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View view)
                                               {
                                                   Toast.makeText(getActivity(), "Going to Light Data", Toast.LENGTH_SHORT).show();
                                                   ((DeviceData) Objects.requireNonNull(getActivity())).setViewPager(1);
                                               }
                                           }
        );
        btnNavFragTemp.setOnClickListener(new View.OnClickListener()
                                          {
                                              @Override
                                              public void onClick(View view)
                                              {
                                                  Toast.makeText(getActivity(), "Going to Temperature Data", Toast.LENGTH_SHORT).show();
                                                  ((DeviceData) Objects.requireNonNull(getActivity())).setViewPager(2);
                                              }
                                          }
        );
        btnNavFragHumid.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View view)
                                               {
                                                   Toast.makeText(getActivity(), "Going to Humidity Data", Toast.LENGTH_SHORT).show();
                                                   ((DeviceData) Objects.requireNonNull(getActivity())).setViewPager(3);
                                               }
                                           }
        );
        btnNavFragMoist.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   Toast.makeText(getActivity(), "Going to Moisture Data", Toast.LENGTH_SHORT).show();
                                                   ((DeviceData) Objects.requireNonNull(getActivity())).setViewPager(4);
                                               }
                                           }
        );

        return view;
    }
}


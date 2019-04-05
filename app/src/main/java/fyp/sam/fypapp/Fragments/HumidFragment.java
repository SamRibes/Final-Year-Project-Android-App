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

import com.jjoe64.graphview.GraphView;

import java.util.Objects;

import fyp.sam.fypapp.Activities.DeviceData;
import fyp.sam.fypapp.DataManagers.SetUpGraphs;
import fyp.sam.fypapp.R;

public class HumidFragment extends Fragment
{
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.data_humid_fragment, container, false);

        GraphView graph = view.findViewById(R.id.humid_graph_view);

        SetUpGraphs.setUpLineGraph(((DeviceData) Objects.requireNonNull(getActivity())).dataForGraphs, ((DeviceData)getActivity()).plant_Limits,  graph, 'h');

        ImageButton btnNavFragAll = (ImageButton) view.findViewById(R.id.nav_button_all);

        TextView textView = view.findViewById(R.id.current_humid);

        String mostRecentValue =  ((DeviceData)getActivity()).dataForGraphs[0][4];

        textView.setText(this.getString(R.string.current_humid) + " " + mostRecentValue);

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


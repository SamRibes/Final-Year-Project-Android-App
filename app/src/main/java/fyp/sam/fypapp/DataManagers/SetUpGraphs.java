package fyp.sam.fypapp.DataManagers;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

import fyp.sam.fypapp.R;

public class SetUpGraphs
{

    public static void setUpBarChart(String[][] dataForGraphs, String[] Plant_Limits, GraphView graphView)
    {
        double humidity_Max = Double.parseDouble(Plant_Limits[1]),
                humidity_Min = Double.parseDouble(Plant_Limits[2]),
                light_Day_Max = Double.parseDouble(Plant_Limits[3]),
                light_Day_Min = Double.parseDouble(Plant_Limits[4]),
                light_Night_Max = Double.parseDouble(Plant_Limits[5]),
                light_Night_Min = Double.parseDouble(Plant_Limits[6]),
                moisture_Max = Double.parseDouble(Plant_Limits[7]),
                moisture_Min = Double.parseDouble(Plant_Limits[8]),
                temp_Day_Max = Double.parseDouble(Plant_Limits[9]),
                temp_Day_Min = Double.parseDouble(Plant_Limits[10]),
                temp_Night_Max = Double.parseDouble(Plant_Limits[11]),
                temp_Night_Min = Double.parseDouble(Plant_Limits[12]);

        double light = Double.valueOf(dataForGraphs[0][2]);
        double temp = Double.valueOf(dataForGraphs[0][3]);
        double humid = Double.valueOf(dataForGraphs[0][4]);
        double moist = Double.valueOf(dataForGraphs[0][5]);

        DataPoint StartingMaxdp;
        DataPoint EndingMaxdp;

        DataPoint StartingMindp;
        DataPoint EndingMindp;

        LineGraphSeries<DataPoint>
                tempMaxSeries = new LineGraphSeries<>(),
                tempMinSeries = new LineGraphSeries<>(),
                humidMaxSeries = new LineGraphSeries<>(),
                humidMinSeries = new LineGraphSeries<>(),
                lightMaxSeries = new LineGraphSeries<>(),
                lightMinSeries = new LineGraphSeries<>(),
                moistMaxSeries = new LineGraphSeries<>(),
                moistMinSeries = new LineGraphSeries<>();

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, light),
                new DataPoint(3, temp),
                new DataPoint(5, humid),
                new DataPoint(7, moist)
        });

        barGraphSeries.setSpacing(75);
        barGraphSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data)
            {
                int col = 0;
                if (data.getX() == 1)
                {
                    col = Color.parseColor("#ffee58");
                }
                if (data.getX() == 3)
                {
                    col = Color.parseColor("#e64a19");
                }
                if (data.getX() == 5)
                {
                    col = Color.parseColor("#bbdefb");
                }
                if (data.getX() == 7)
                {
                    col = Color.parseColor("#42a5f5");
                }
                return col;
            }
        });

        // draw values on top
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setValuesOnTopColor(R.color.colorPrimaryDark);

        for (int i = 0; i < 4; i++)
        {
            double currentFactorValueMax = 0, currentFactorValueMin = 0;
            LineGraphSeries<DataPoint> temporaryMaxSeries = null, temporaryMinSeries = null;
            if(i == 0){
                temporaryMaxSeries = lightMaxSeries;
                temporaryMinSeries = lightMinSeries;
                if(GetIsItDay())
                {
                    currentFactorValueMax = light_Day_Max;
                    currentFactorValueMin = light_Day_Min;
                }
                else
                {
                    currentFactorValueMax = light_Night_Max;
                    currentFactorValueMin = light_Night_Min;
                }
            }
            if(i == 1)
            {
                temporaryMaxSeries = tempMaxSeries;
                temporaryMinSeries = tempMinSeries;
                if(GetIsItDay())
                {
                    currentFactorValueMax = temp_Day_Max;
                    currentFactorValueMin = temp_Day_Min;
                }
                else
                {
                    currentFactorValueMax = temp_Night_Max;
                    currentFactorValueMin = temp_Night_Min;
                }
            }
            if(i == 2){
                currentFactorValueMax = humidity_Max;
                currentFactorValueMin = humidity_Min;
                temporaryMaxSeries = humidMaxSeries;
                temporaryMinSeries = humidMinSeries;
            }
            if(i == 3){
                currentFactorValueMax = moisture_Max;
                currentFactorValueMin = moisture_Min;
                temporaryMaxSeries = moistMaxSeries;
                temporaryMinSeries = moistMinSeries;
            }

            StartingMaxdp = new DataPoint(((i * 2) + 0.5), currentFactorValueMax);
            EndingMaxdp = new DataPoint(((i * 2) + 1.5), currentFactorValueMax);

            StartingMindp = new DataPoint(((i * 2) + 0.5), currentFactorValueMin);
            EndingMindp = new DataPoint(((i * 2) + 1.5), currentFactorValueMin);

            temporaryMaxSeries.setColor(Color.RED);
            temporaryMinSeries.setColor(Color.RED);
            temporaryMaxSeries.setThickness(10);
            temporaryMinSeries.setThickness(10);

            temporaryMaxSeries.appendData(StartingMaxdp, true, 2);
            temporaryMaxSeries.appendData(EndingMaxdp, true, 2);

            temporaryMinSeries.appendData(StartingMindp, true, 2);
            temporaryMinSeries.appendData(EndingMindp, true, 2);

        }

        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(8);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(100);

        graphView.addSeries(lightMaxSeries);
        graphView.addSeries(lightMinSeries);
        graphView.addSeries(tempMaxSeries);
        graphView.addSeries(tempMinSeries);
        graphView.addSeries(humidMaxSeries);
        graphView.addSeries(humidMinSeries);
        graphView.addSeries(moistMaxSeries);
        graphView.addSeries(moistMinSeries);

        graphView.addSeries(barGraphSeries);
    }


    public static void setUpLineGraph(String[][] dataForGraphs, String[] Plant_Limits, GraphView graphView, char graphType) {
        double Humidity_Max = Double.parseDouble(Plant_Limits[1]),
                Humidity_Min = Double.parseDouble(Plant_Limits[2]),
                Light_Day_Max = Double.parseDouble(Plant_Limits[3]),
                Light_Day_Min = Double.parseDouble(Plant_Limits[4]),
                Light_Night_Max = Double.parseDouble(Plant_Limits[5]),
                Light_Night_Min = Double.parseDouble(Plant_Limits[6]),
                Moisture_Max = Double.parseDouble(Plant_Limits[7]),
                Moisture_Min = Double.parseDouble(Plant_Limits[8]),
                Temp_Day_Max = Double.parseDouble(Plant_Limits[9]),
                Temp_Day_Min = Double.parseDouble(Plant_Limits[10]),
                Temp_Night_Max = Double.parseDouble(Plant_Limits[11]),
                Temp_Night_Min = Double.parseDouble(Plant_Limits[12]);

        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> maxSeries = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> minSeries = new LineGraphSeries<>();

        int j = 0;
        for (int i = 24; i > -1; i--)
        {
            DataPoint dp = null;
            if(dataForGraphs[i][0].equals("Latest"))
            {
                //Skip Latest
            }
            else
            {
                switch (graphType)
                {
                    case 'l':
                        double light = Double.valueOf(dataForGraphs[i][2]);
                        dp = new DataPoint(j+1, light);
                        break;
                    case 't':
                        double temp = Double.valueOf(dataForGraphs[i][3]);
                        dp = new DataPoint(j+1, temp);
                        break;
                    case 'h':
                        double humid = Double.valueOf(dataForGraphs[i][4]);
                        dp = new DataPoint(j+1, humid);
                        break;
                    case 'm':
                        double moist = Double.valueOf(dataForGraphs[i][5]);
                        dp = new DataPoint(j+1, moist);
                        break;
                }
                dataSeries.appendData(dp, true, 24);
            }
            j++;
        }
        dataSeries.setColor(R.color.colorPrimaryDark);

        DataPoint StartingMaxdp = null;
        DataPoint EndingMaxdp = null;

        DataPoint StartingMindp = null;
        DataPoint EndingMindp = null;

        switch (graphType)
        {
            case 't':
                if(GetIsItDay()) {
                    StartingMaxdp = new DataPoint(1, Temp_Day_Max);
                    EndingMaxdp = new DataPoint(24, Temp_Day_Max);

                    StartingMindp = new DataPoint(1, Temp_Day_Min);
                    EndingMindp = new DataPoint(24, Temp_Day_Min);
                }
                else
                {
                    StartingMaxdp = new DataPoint(1, Temp_Night_Max);
                    EndingMaxdp = new DataPoint(24, Temp_Night_Max);

                    StartingMindp = new DataPoint(1, Temp_Night_Min);
                    EndingMindp = new DataPoint(24, Temp_Night_Min);
                }
                graphView.removeAllSeries();
                graphView.getGridLabelRenderer().setHorizontalAxisTitle("Time (hrs)");
                graphView.getGridLabelRenderer().setVerticalAxisTitle("Temperature (C)°");

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);
                graphView.getViewport().setMaxY(50);
                break;
            case 'h':
                StartingMaxdp = new DataPoint(1, Humidity_Max);
                EndingMaxdp = new DataPoint(24, Humidity_Max);

                StartingMindp = new DataPoint(1, Humidity_Min);
                EndingMindp = new DataPoint(24, Humidity_Min);

                graphView.removeAllSeries();
                graphView.getGridLabelRenderer().setHorizontalAxisTitle("Time (hrs)");
                graphView.getGridLabelRenderer().setVerticalAxisTitle("Humidity (%)");

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);
                graphView.getViewport().setMaxY(100);
                break;
            case 'l':
                if(GetIsItDay()) {
                    StartingMaxdp = new DataPoint(1, Light_Day_Max);
                    EndingMaxdp = new DataPoint(24, Light_Day_Max);

                    StartingMindp = new DataPoint(1, Light_Day_Min);
                    EndingMindp = new DataPoint(24, Light_Day_Min);
                }
                else
                {
                    StartingMaxdp = new DataPoint(1, Light_Night_Max);
                    EndingMaxdp = new DataPoint(24, Light_Night_Max);

                    StartingMindp = new DataPoint(1, Light_Night_Min);
                    EndingMindp = new DataPoint(24, Light_Night_Min);
                }

                graphView.removeAllSeries();
                graphView.getGridLabelRenderer().setHorizontalAxisTitle("Time (hrs)");
                graphView.getGridLabelRenderer().setVerticalAxisTitle("Light (%)");

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);
                graphView.getViewport().setMaxY(100);
                break;
            case 'm':
                StartingMaxdp = new DataPoint(1, Moisture_Max);
                EndingMaxdp = new DataPoint(24, Moisture_Max);

                StartingMindp = new DataPoint(1, Moisture_Min);
                EndingMindp = new DataPoint(24, Moisture_Min);

                graphView.removeAllSeries();
                graphView.getGridLabelRenderer().setHorizontalAxisTitle("Time (hrs)");
                graphView.getGridLabelRenderer().setVerticalAxisTitle("Moisture (%)");

                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0);
                graphView.getViewport().setMaxY(100);
                break;
        }

        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScalableY(false);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(24);


        maxSeries.setColor(Color.RED);
        minSeries.setColor(Color.RED);

        maxSeries.appendData(StartingMaxdp, true, 2);
        maxSeries.appendData(EndingMaxdp, true, 2);

        minSeries.appendData(StartingMindp, true, 24);
        minSeries.appendData(EndingMindp, true, 24);

        DataPoint[] maxDPArray = {StartingMaxdp, EndingMaxdp};
        DataPoint[] minDPArray = {StartingMindp, EndingMindp};

        maxSeries.resetData(maxDPArray);
        maxSeries.setThickness(10);
        maxSeries.setDrawBackground(false);

        minSeries.resetData(minDPArray);
        minSeries.setThickness(10);
        minSeries.setDrawBackground(false);

        graphView.addSeries(maxSeries);
        graphView.addSeries(minSeries);
        graphView.addSeries(dataSeries);
    }

    //Method to see if it is daytime or nighttime
    private static boolean GetIsItDay() {
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        //return hourOfDay > 6 && hourOfDay < 18;
        return true;
    }

}

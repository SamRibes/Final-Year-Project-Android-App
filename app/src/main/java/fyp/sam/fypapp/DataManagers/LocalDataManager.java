package fyp.sam.fypapp.DataManagers;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Objects;

public class LocalDataManager
{
    private static FileInputStream  finalPlantLimitInputStream = null;
    private static FileInputStream  finalSensorDataInputStream = null;


    public static void initialiseSensorDataInputStream(FileInputStream PlantLimitInputStream)
    {
        finalSensorDataInputStream = PlantLimitInputStream;
    }

    public static void initialisePlantLimitsInputStream(FileInputStream SensorDataInputStream)
    {
        finalPlantLimitInputStream = SensorDataInputStream;
    }

    static void UpdateLocalData(QueryDocumentSnapshot document, BufferedWriter bw)
    {
        try
        {
            Date parsedtimestamp = Objects.requireNonNull(document.getTimestamp("timestamp")).toDate();
            long dateToLong = parsedtimestamp.getTime();
                    bw.write((
                    (Integer.parseInt(document.getId())) + ", " +
                    dateToLong + ", " +
                    document.get("light") + ", " +
                    document.get("temp") + ", " +
                    document.get("humidity") + ", " +
                    document.get("moisture") + '\n'));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void UpdatePlantData(DocumentSnapshot document, BufferedWriter bw)
    {
        try
        {
            String stringToWrite = (((
                    document.getId()) + ", " +
                    document.get("Humidity_Max") + ", " +
                    document.get("Humidity_Min") + ", " +
                    document.get("Light_Day_Max") + ", " +
                    document.get("Light_Day_Min") + ", " +
                    document.get("Light_Night_Max") + ", " +
                    document.get("Light_Night_Min") + ", " +
                    document.get("Moisture_Max") + ", " +
                    document.get("Moisture_Min") + ", " +
                    document.get("Temp_Day_Max") + ", " +
                    document.get("Temp_Day_Min") + ", " +
                    document.get("Temp_Night_Max") + ", " +
                    document.get("Temp_Night_Min")
            ));
            bw.write(stringToWrite);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[][] getLocalSensorData()
    {
        String[][] LocalSensorData =  new String[12][6];
        String delims = "[, ]+";

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(finalSensorDataInputStream));
            String fullLine;
            int i = 0;
            while ((fullLine = br.readLine()) != null)
            {
                String splitLine[] = fullLine.split(delims);

                //int currentID = Integer.parseInt(splitLine[0]);

                LocalSensorData[i][0] =  splitLine[0];
                LocalSensorData[i][1] =  splitLine[1];
                LocalSensorData[i][2] =  splitLine[2];
                LocalSensorData[i][3] =  splitLine[3];
                LocalSensorData[i][4] =  splitLine[4];
                LocalSensorData[i][5] =  splitLine[5];
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LocalSensorData;
    }

    public static String[] getLocalPlant_Limits()
    {
        String[] LocalPlantLimits =  new String[10];
        String delims = "[, ]+";

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(finalPlantLimitInputStream));
            String fullLine;

            while ((fullLine = br.readLine()) != null)
            {
                LocalPlantLimits = fullLine.split(delims);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LocalPlantLimits;
    }
}

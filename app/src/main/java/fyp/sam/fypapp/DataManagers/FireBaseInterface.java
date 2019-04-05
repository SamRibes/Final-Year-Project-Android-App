package fyp.sam.fypapp.DataManagers;

import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

import fyp.sam.fypapp.Activities.DeviceData;

import static android.content.ContentValues.TAG;

public class FireBaseInterface
{
    private static FileOutputStream finalSensorDataOutputStream = null;
    private static FileOutputStream finalPlantLimitsOutputStream = null;


    public static void initialiseSensorDataOutputStream(FileOutputStream sensorDataOutputStream)
    {
        finalSensorDataOutputStream = sensorDataOutputStream;
    }

    public static void initialisePlantLimitsOutputStream(FileOutputStream plantLimitsOutputStream)
    {
        finalPlantLimitsOutputStream = plantLimitsOutputStream;
    }

    public static void getDataFromServer(String collectionPath) {
        if(collectionPath.equals("SensorData"))
        {
            DeviceData.db.collection(collectionPath)
                    .orderBy("timestamp", Query.Direction.DESCENDING).
                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(finalSensorDataOutputStream));
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            LocalDataManager.UpdateLocalData(document, bw);
                            /*if(document.getId().equals("Latest"))
                            {
                                System.out.println("Skipped " + document.getId());
                            }
                            else
                            {
                                LocalDataManager.UpdateLocalData(document, bw);
                            }*/
                        }
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else
        {
            Log.d(Constraints.TAG, "Happens");
            DocumentReference docRef = DeviceData.db.collection(collectionPath).document("Peace_Lily");

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(finalPlantLimitsOutputStream));
                        DocumentSnapshot document = task.getResult();

                        LocalDataManager.UpdatePlantData(Objects.requireNonNull(document), bw);
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });
        }
    }
}

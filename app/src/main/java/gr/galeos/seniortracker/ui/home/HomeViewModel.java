package gr.galeos.seniortracker.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeViewModel extends ViewModel {

    FirebaseDatabase database;
    DatabaseReference df;

    public HomeViewModel() {
       database = FirebaseDatabase.getInstance();
    }

    public void writeLocationData(String id,double lon, double lat) {
        df = database.getReference("users").child(id);
        df.child("lat").setValue(lat);
        df.child("lon").setValue(lon);
    }

    public void getLocationData() {
        df = database.getReference("users").child("OVsxrDNogjdGws1x5y1G5RSP4Vl2").child("lat");
        df.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Lat", task.getResult().getValue().toString());
            } else {
                System.out.println(task.getException().getMessage());
            }
        });
        df = database.getReference("users").child("OVsxrDNogjdGws1x5y1G5RSP4Vl2").child("lon");
        df.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Lon", task.getResult().getValue().toString());
            } else {
                System.out.println(task.getException().getMessage());
            }
        });
    }

    public void getSeniors() {

    }

}
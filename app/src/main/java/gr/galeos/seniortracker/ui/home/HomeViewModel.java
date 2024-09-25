package gr.galeos.seniortracker.ui.home;

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

}
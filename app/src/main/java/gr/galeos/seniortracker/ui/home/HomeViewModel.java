package gr.galeos.seniortracker.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.UserModel;

public class HomeViewModel extends ViewModel {

    FirebaseDatabase database;
    FirebaseFirestore db;
    DatabaseReference df;

    private final MutableLiveData<Boolean> _accountFound = new MutableLiveData<>();

    public LiveData<Boolean> findAccount() {
        return _accountFound;
    }

    public HomeViewModel() {
       database = FirebaseDatabase.getInstance();
       db = FirebaseFirestore.getInstance();
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

    public void getMe(String id){
        db.collection("users").document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        UserModel.getInstance().setUser(
                                task.getResult().get("id", String.class),
                                task.getResult().get("firstname", String.class),
                                task.getResult().get("lastname", String.class),
                                task.getResult().get("email", String.class),
                                task.getResult().get("phone", String.class),
                                task.getResult().get("accountType", String.class));
                        _accountFound.setValue(true);
                    } else {
                        _accountFound.setValue(false);
                    }
                });
    }
}
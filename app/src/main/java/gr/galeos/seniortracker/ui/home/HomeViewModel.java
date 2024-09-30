package gr.galeos.seniortracker.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.User;

public class HomeViewModel extends ViewModel {

    FirebaseDatabase database;
    FirebaseFirestore db;
    DatabaseReference df;

    private final MutableLiveData<Boolean> _accountFound = new MutableLiveData<>();

    public LiveData<Boolean> findAccount() {
        return _accountFound;
    }

    private final MutableLiveData<ArrayList<User>> _seniors = new MutableLiveData<>();

    public LiveData<ArrayList<User>> getSeniors() {
        return _seniors;
    }

    public HomeViewModel() {
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void writeLocationData(String id, double lon, double lat) {
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

    public void getMe(String id) {
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

    public void getMySeniors() {
        db.collection("senior_tracking").document(UserModel.getInstance().user.getEmail()).collection("my_seniors").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<User> seniors = new ArrayList<>();
                        for (int i = 0; i < task.getResult().size(); i++) {
                            User user = new User(
                                    task.getResult().getDocuments().get(i).get("id", String.class),
                                    task.getResult().getDocuments().get(i).get("firstname", String.class),
                                    task.getResult().getDocuments().get(i).get("lastname", String.class),
                                    task.getResult().getDocuments().get(i).get("email", String.class),
                                    task.getResult().getDocuments().get(i).get("phone", String.class),
                                    task.getResult().getDocuments().get(i).get("accountType", String.class)
                            );
                            SeniorModel.getInstance().user = user;
                            seniors.add(user);
                        }
                        _seniors.postValue(seniors);
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }
}
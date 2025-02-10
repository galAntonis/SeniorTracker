package gr.galeos.seniortracker.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.GeofenceModel;
import gr.galeos.seniortracker.models.LocationModel;
import gr.galeos.seniortracker.models.User;

public class HomeViewModel extends ViewModel {

    FirebaseDatabase database;
    FirebaseFirestore db;

    private final MutableLiveData<Boolean> _accountFound = new MutableLiveData<>();

    public LiveData<Boolean> findAccount() {
        return _accountFound;
    }

    private final MutableLiveData<ArrayList<User>> _seniors = new MutableLiveData<>();

    public LiveData<ArrayList<User>> getSeniors() {
        return _seniors;
    }

    private final MutableLiveData<ArrayList<GeofenceModel>> _geofences = new MutableLiveData<>();

    public LiveData<ArrayList<GeofenceModel>> getGeofences() {
        return _geofences;
    }

    private final MutableLiveData<List<LocationModel>> locationsLiveData = new MutableLiveData<>();

    public LiveData<List<LocationModel>> getLocationsLiveData() {
        return locationsLiveData;
    }

    public HomeViewModel() {
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
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
                        fetchLocationData();
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }

    public void getMyGeofences() {
        db.collection("geofences")
                .document(UserModel.getInstance().user.getEmail())
                .collection("my_geofences")
                .get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()) {
                        ArrayList<GeofenceModel> geofences = new ArrayList<>();
                        UserModel.getInstance().geofences.clear();
                        Log.d("HomeViewModel", "Size " +task.getResult().size());
                        for (int i = 0; i < task.getResult().size(); i++) {
                            GeofenceModel geofence = new GeofenceModel(
                                task.getResult().getDocuments().get(i).get("name", String.class),
                                task.getResult().getDocuments().get(i).get("latitude", Double.class),
                                task.getResult().getDocuments().get(i).get("longitude", Double.class),
                                task.getResult().getDocuments().get(i).get("radius", Integer.class)
                            );
                            UserModel.getInstance().geofences.add(geofence);
                            geofences.add(geofence);
                        }
                        _geofences.postValue(geofences);
                    } else {
                        _geofences.postValue(null);
                        Toast.makeText(null, "No geofences found", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void fetchLocationData() {
        long twoHoursAgo = System.currentTimeMillis() - (2 * 60 * 60 * 1000);  // Current time minus 2 hours

        FirebaseDatabase.getInstance().getReference("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LocationModel> locationList = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // Loop through all users
                    for (DataSnapshot locationSnapshot : userSnapshot.getChildren()) { // Loop through each user's locations
                        LocationModel location = locationSnapshot.getValue(LocationModel.class);
                        if (location != null && location.name != null && !location.name.trim().isEmpty() && location.timestamp >= twoHoursAgo) {
                            locationList.add(location);  // Only add valid locations
                        }
                    }
                }
                locationsLiveData.setValue(locationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

}
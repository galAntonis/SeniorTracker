package gr.galeos.seniortracker.ui.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.LocationModel;
import gr.galeos.seniortracker.models.User;

public class MapsViewModel extends ViewModel {
    // LiveData to hold location data list
    private final MutableLiveData<LocationModel> lastLocationLiveData = new MutableLiveData<>();

    public LiveData<LocationModel> getLastLocationsLiveData() {
        return lastLocationLiveData;
    }

    private final MutableLiveData<ArrayList<User>> _seniors = new MutableLiveData<>();

    public LiveData<ArrayList<User>> getSeniors() {
        return _seniors;
    }

    // Firebase Database reference
    private DatabaseReference databaseReference;
    FirebaseFirestore db;


    public MapsViewModel() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
        db = FirebaseFirestore.getInstance();

    }

    public void fetchLastLocationData(User senior) {
        databaseReference.child(senior.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LocationModel lastLocation = null;

                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    LocationModel location = locationSnapshot.getValue(LocationModel.class);
                    if (location != null) {
                        // Keep updating lastLocation until we have the latest one
                        lastLocation = location;
                    }
                }

                if (lastLocation != null) {
                    // Update LiveData with the most recent location
                    lastLocationLiveData.setValue(lastLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
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
                        UserModel.getInstance().seniors.addAll(seniors);
                        _seniors.postValue(seniors);
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }

}
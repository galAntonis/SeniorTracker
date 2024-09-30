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

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.models.LocationModel;

public class MapsViewModel extends ViewModel {
    // LiveData to hold location data list
    private final MutableLiveData<LocationModel> lastLocationLiveData = new MutableLiveData<>();

    // Firebase Database reference
    private DatabaseReference databaseReference;

    public MapsViewModel() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
    }

    public void fetchLastLocationData() {
        databaseReference.child(SeniorModel.getInstance().user.getId()).addValueEventListener(new ValueEventListener() {
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
    public LiveData<LocationModel> getLastLocationsLiveData() {
        return lastLocationLiveData;
    }

}
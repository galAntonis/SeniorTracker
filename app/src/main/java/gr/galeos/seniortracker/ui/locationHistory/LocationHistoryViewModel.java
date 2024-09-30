package gr.galeos.seniortracker.ui.locationHistory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.models.LocationModel;

public class LocationHistoryViewModel extends ViewModel {

    // LiveData to hold location data list
    private final MutableLiveData<List<LocationModel>> locationsLiveData = new MutableLiveData<>();

    // Firebase Database reference
    private DatabaseReference databaseReference;

    public LocationHistoryViewModel() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
        fetchLocationData();
    }

    // Function to fetch location data from Firebase
    private void fetchLocationData() {
        long twoHoursAgo = System.currentTimeMillis() - (2 * 60 * 60 * 1000);  // Current time minus 2 hours in milliseconds

        databaseReference.child(SeniorModel.getInstance().user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LocationModel> locationList = new ArrayList<>();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    LocationModel location = locationSnapshot.getValue(LocationModel.class);
                    if (location != null && location.timestamp >= twoHoursAgo) {
                        locationList.add(location);  // Only add locations from the last 2 hours
                    }
                }
                // Update LiveData with the fetched locations
                locationsLiveData.setValue(locationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }


    // Expose LiveData to the fragment
    public LiveData<List<LocationModel>> getLocationsLiveData() {
        return locationsLiveData;
    }
}
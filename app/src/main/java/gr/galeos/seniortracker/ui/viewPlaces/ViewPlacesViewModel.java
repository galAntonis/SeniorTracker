package gr.galeos.seniortracker.ui.viewPlaces;

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

import gr.galeos.seniortracker.models.LocationModel;


public class ViewPlacesViewModel extends ViewModel {

    // LiveData to hold location data list
    private final MutableLiveData<List<LocationModel>> locationsLiveData = new MutableLiveData<>();

    // Firebase Database reference
    private DatabaseReference databaseReference;

    public ViewPlacesViewModel() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
        fetchLocationData();
    }

    // Function to fetch location data from Firebase
    private void fetchLocationData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LocationModel> locationList = new ArrayList<>();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    LocationModel location = locationSnapshot.getValue(LocationModel.class);
                    if (location != null) {
                        locationList.add(location);
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

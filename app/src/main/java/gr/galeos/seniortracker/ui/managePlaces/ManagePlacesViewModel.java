package gr.galeos.seniortracker.ui.managePlaces;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagePlacesViewModel extends ViewModel {

    // LiveData to hold the current location
    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    // Firebase Database reference
    private DatabaseReference databaseReference;

    public ManagePlacesViewModel() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
    }
    // Function to expose location LiveData to the Fragment
    public LiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }
}
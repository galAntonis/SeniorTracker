package gr.galeos.seniortracker.ui.managePlaces;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import gr.galeos.seniortracker.models.GeofenceModel;

public class ManagePlacesViewModel extends ViewModel {

    private FirebaseFirestore db;
    private final MutableLiveData<Pair<List<GeofenceModel>, List<String>>> geofencesLiveData = new MutableLiveData<>();

    public ManagePlacesViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void fetchGeofences(String email) {
        db.collection("geofences")
                .document(email)
                .collection("my_geofences")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<GeofenceModel> geofences = new ArrayList<>();
                    List<String> documentIds = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        // Assuming the GeofenceModel can be created from the document data
                        String name = document.getString("name");
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        int radius = document.getLong("radius").intValue();  // Firestore returns long for integers

                        GeofenceModel geofenceModel = new GeofenceModel(name, latitude, longitude, radius);
                        geofences.add(geofenceModel);
                        documentIds.add(document.getId());  // Store the Firestore document ID
                    }
                    // Pass both the geofences and their corresponding document IDs to the UI
                    geofencesLiveData.setValue(new Pair<>(geofences, documentIds));
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    // Getter for LiveData to observe in the fragment
    public LiveData<Pair<List<GeofenceModel>, List<String>>> getGeofencesLiveData() {
        return geofencesLiveData;
    }
}

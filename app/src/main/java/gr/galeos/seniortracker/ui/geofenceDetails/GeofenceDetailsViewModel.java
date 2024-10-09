package gr.galeos.seniortracker.ui.geofenceDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.GeofenceModel;

public class GeofenceDetailsViewModel extends ViewModel {

    FirebaseFirestore db;

    private final MutableLiveData<Boolean> _found = new MutableLiveData<>();

    public LiveData<Boolean> findMySenior() {
        return _found;
    }

    private final MutableLiveData<Boolean> _storedGeofence = new MutableLiveData<>();

    public LiveData<Boolean> storeGeofence() {
        return _storedGeofence;
    }

    public GeofenceDetailsViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    // Existing function to find senior
    public void findSenior(String email) {
        db.collection("senior_tracking")
                .document(UserModel.getInstance().user.getEmail())
                .collection("my_seniors")
                .document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        _found.setValue(true);
                    } else {
                        _found.setValue(false);
                    }
                });
    }

    // Updated function to write GeofenceModel to Firestore
    public void writeGeofenceToFirestore(String email,String name, GeofenceModel geofence) {
        db.collection("geofences")
                .document(email)
                .collection("my_geofences")
                .document()
                .set(geofence.toMap())
                .addOnSuccessListener(aVoid -> {
                    _storedGeofence.setValue(true);
                })
                .addOnFailureListener(e -> {
                    _storedGeofence.setValue(false);
                });
    }
}

package gr.galeos.seniortracker.ui.editSenior;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.models.User;


public class EditSeniorViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _userData = new MutableLiveData<>();
    public LiveData<Boolean> updateUserData() { return _userData; }

    private final MutableLiveData<User> _seniorData = new MutableLiveData<>();

    public LiveData<User> getSeniorData() {
        return _seniorData;
    }

    private DatabaseReference databaseReference;
    FirebaseFirestore db;

    public EditSeniorViewModel() {
        db = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("seniors"); // Adjust this path as needed
    }


    public void fetchSeniorDetails(String seniorId) {
        db.collection("users").document(seniorId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        SeniorModel.getInstance().setUser(
                                task.getResult().get("id", String.class),
                                task.getResult().get("firstname", String.class),
                                task.getResult().get("lastname", String.class),
                                task.getResult().get("email", String.class),
                                task.getResult().get("phone", String.class),
                                task.getResult().get("accountType", String.class));
                        _seniorData.setValue(SeniorModel.getInstance().user);
                    } else {
                        _seniorData.setValue(null);
                    }
                });
    }

    public void updateUserData(String id, String firstname, String lastname, String email, String phone, String accountType) {
        SeniorModel.getInstance().setUser(id, firstname, lastname, email, phone, accountType);

        if (accountType.equals("1")) {
            db.collection("seniors").document(email).set(SeniorModel.getInstance().user);
        }
        db.collection("users").document(id).set(SeniorModel.getInstance().user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _userData.postValue(true);
                    } else {
                        _userData.postValue(false);
                    }
                });
    }
}
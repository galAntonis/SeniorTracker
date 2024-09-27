package gr.galeos.seniortracker.ui.addSenior;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.User;

public class AddSeniorViewModel extends ViewModel {

    private final MutableLiveData<User> _user = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return _user;
    }

    private final MutableLiveData<Boolean> _userAdded = new MutableLiveData<>();

    public LiveData<Boolean> getUserAdded() {
        return _userAdded;
    }

    FirebaseFirestore db;

    public AddSeniorViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void findUser(String email) {
        db.collection("seniors").document(email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(
                                task.getResult().get("id", String.class),
                                task.getResult().get("firstname", String.class),
                                task.getResult().get("lastname", String.class),
                                task.getResult().get("email", String.class),
                                task.getResult().get("phone", String.class),
                                task.getResult().get("accountType", String.class)
                        );
                        _user.postValue( user);
                    } else {
                        _user.postValue(null);
                    }
                });
    }

    public void addSenior(String email) {
        Map<String,String> senior = Map.of("email", email);
        db.collection("senior_tracking").document(UserModel.getInstance().user.getEmail()).set(senior)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _userAdded.postValue(true);
                    } else {
                        _userAdded.postValue(false);
                    }
                });
    }
}
package gr.galeos.seniortracker.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.UserModel;
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
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }
}
package gr.galeos.seniortracker.ui.manageSeniors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.User;

public class ManageSeniorsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<User>> _seniors = new MutableLiveData<>();
    public LiveData<ArrayList<User>> getSeniors() {
        return _seniors;
    }

    FirebaseFirestore db;

    public ManageSeniorsViewModel() {
        db = FirebaseFirestore.getInstance();
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
                            seniors.add(user);
                        }
                        _seniors.postValue(seniors);
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }

}
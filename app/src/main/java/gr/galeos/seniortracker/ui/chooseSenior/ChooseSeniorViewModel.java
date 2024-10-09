package gr.galeos.seniortracker.ui.chooseSenior;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gr.galeos.seniortracker.UserModel;

public class ChooseSeniorViewModel extends ViewModel {

    FirebaseFirestore db;

    private final MutableLiveData<ArrayList<String>> _seniors = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getSeniors() {
        return _seniors;
    }

    public ChooseSeniorViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void getMySeniors() {
        db.collection("senior_tracking").document(UserModel.getInstance().user.getEmail()).collection("my_seniors").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> seniors = new ArrayList<>();
                        for (int i = 0; i < task.getResult().size(); i++) {
                            seniors.add(task.getResult().getDocuments().get(i).get("email", String.class));
                        }
                        _seniors.postValue(seniors);
                    } else {
                        _seniors.postValue(null);
                    }
                });
    }
}
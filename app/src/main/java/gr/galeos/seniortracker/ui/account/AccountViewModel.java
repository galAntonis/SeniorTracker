package gr.galeos.seniortracker.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.models.User;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _data = new MutableLiveData<>();
    public LiveData<Boolean> data() { return _data; }

    FirebaseFirestore db;

    public AccountViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeUserData(String id, String firstname, String lastname, String email, String phone, Integer accountType) {
        db.collection("users").document(id).set(new User(id, firstname, lastname, email, phone, accountType))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _data.postValue(true);
                    } else {
                        _data.postValue(false);
                    }
                });
    }

}
package gr.galeos.seniortracker.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.UserModel;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _data = new MutableLiveData<>();
    public LiveData<Boolean> data() { return _data; }

    private final MutableLiveData<String> _accountType = new MutableLiveData<>();
    public LiveData<String> getType() { return _accountType; }

    FirebaseFirestore db;

    public AccountViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeUserData(String id, String firstname, String lastname, String email, String phone, String accountType) {
        UserModel.getInstance().setUser(id, firstname, lastname, email, phone, accountType);
        db.collection("users").document(id).set(UserModel.getInstance().user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _data.postValue(true);
                    } else {
                        _data.postValue(false);
                    }
                });
    }

    public void getAccountType(String id) {
        db.collection("users").document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _accountType.postValue(task.getResult().get("accountType", String.class));
                    } else {
                        _accountType.postValue(task.getException().getMessage());
                    }
                });
    }

}
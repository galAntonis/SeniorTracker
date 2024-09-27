package gr.galeos.seniortracker.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.UserModel;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _userData = new MutableLiveData<>();
    public LiveData<Boolean> createdUserData() { return _userData; }


    FirebaseFirestore db;

    public AccountViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeUserData(String id, String firstname, String lastname, String email, String phone, String accountType) {
        UserModel.getInstance().setUser(id, firstname, lastname, email, phone, accountType);

        if (accountType.equals("1")) {
            db.collection("seniors").document(email).set(UserModel.getInstance().user);
        }
        db.collection("users").document(id).set(UserModel.getInstance().user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _userData.postValue(true);
                    } else {
                        _userData.postValue(false);
                    }
                });
    }


}
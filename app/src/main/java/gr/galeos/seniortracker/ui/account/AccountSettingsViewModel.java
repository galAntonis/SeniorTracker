package gr.galeos.seniortracker.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.models.User;

public class AccountSettingsViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _userData = new MutableLiveData<>();
    public LiveData<Boolean> updateUserData() { return _userData; }

    private final MutableLiveData<User> _accountData = new MutableLiveData<>();

    public LiveData<User> getAccountData() {
        return _accountData;
    }

    FirebaseFirestore db;

    public AccountSettingsViewModel() {
        db = FirebaseFirestore.getInstance();
    }


    public void fetchAccountDetails(String id) {
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
                        _accountData.setValue(UserModel.getInstance().user);
                    } else {
                        _accountData.setValue(null);
                    }
                });
    }

    public void updateUserData(String id, String firstname, String lastname, String email, String phone, String accountType) {
        UserModel.getInstance().setUser(id, firstname, lastname, email, phone, accountType);

        if (accountType.equals("1")) {
            db.collection("seniors").document(email).set(SeniorModel.getInstance().user);
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
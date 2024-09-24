package gr.galeos.seniortracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<String> _accessToken = new MutableLiveData<>();

    public LiveData<String> getToken() {
        return _accessToken;
    }

    private final MutableLiveData<String> _accountType = new MutableLiveData<>();

    public LiveData<String> getType() {return _accountType;}

    FirebaseFirestore db;

    private final FirebaseAuth firebaseAuth;

    public LoginViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                _accessToken.postValue(firebaseAuth.getUid());
                            } else {
                                _accessToken.postValue(null);
                            }
                        }
                );
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
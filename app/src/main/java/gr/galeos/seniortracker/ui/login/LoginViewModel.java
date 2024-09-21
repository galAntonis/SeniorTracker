package gr.galeos.seniortracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<String> _accessToken = new MutableLiveData<>();

    public LiveData<String> getToken() {
        return _accessToken;
    }

    private final FirebaseAuth firebaseAuth;

    public LoginViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
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

}
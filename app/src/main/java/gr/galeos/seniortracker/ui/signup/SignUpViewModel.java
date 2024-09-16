package gr.galeos.seniortracker.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<Boolean> _signup = new MutableLiveData<>();
    public LiveData<Boolean> getSignup() {
        return _signup;
    }

    private FirebaseAuth firebaseAuth;

    public SignUpViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _signup.postValue(true);
                    } else {
                        _signup.postValue(false);
                    }
                });
    }
}
package gr.galeos.seniortracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _isLoggedIn = new MutableLiveData<>();

    public LiveData<Boolean> getLoggedInStatus() {
        return _isLoggedIn;
    }

    public MainViewModel() {
    }

    public void userLoggedIn() {
        if (SharedPreferencesUtils.isSessionIdValid()){
            _isLoggedIn.postValue(true);
        }else if (!SharedPreferencesUtils.isSessionIdValid()){
            _isLoggedIn.postValue(false);
        }
    }
}

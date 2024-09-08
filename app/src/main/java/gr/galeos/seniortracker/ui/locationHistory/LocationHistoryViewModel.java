package gr.galeos.seniortracker.ui.locationHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationHistoryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocationHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is location history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
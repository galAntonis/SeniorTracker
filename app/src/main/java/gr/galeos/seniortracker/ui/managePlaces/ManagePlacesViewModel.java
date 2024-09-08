package gr.galeos.seniortracker.ui.managePlaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManagePlacesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManagePlacesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manage places fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package gr.galeos.seniortracker.ui.manageSeniors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageSeniorsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManageSeniorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manage seniors fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
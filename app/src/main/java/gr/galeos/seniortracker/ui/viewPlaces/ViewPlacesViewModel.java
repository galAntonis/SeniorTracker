package gr.galeos.seniortracker.ui.viewPlaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewPlacesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ViewPlacesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is view places fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
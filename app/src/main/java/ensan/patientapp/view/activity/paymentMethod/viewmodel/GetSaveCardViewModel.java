package ensan.patientapp.view.activity.paymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.paymentMethod.model.GetSaveCard;
import ensan.patientapp.view.activity.paymentMethod.repository.GetSaveCardRepository;


public class GetSaveCardViewModel extends AndroidViewModel {

    private LiveData<GetSaveCard> getSaveCardLiveData;
    private GetSaveCardRepository getSaveCardRepository;

    public GetSaveCardViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getSaveCardRepository = new GetSaveCardRepository();
        getSaveCardLiveData = getSaveCardRepository.getSaveCardLiveData();
    }

    public void getSaveCard(@NonNull String token) {
        getSaveCardRepository.getSaveCard(token);
    }

    public LiveData<GetSaveCard> getVolumesResponseLiveData() {
        return getSaveCardLiveData;
    }
}

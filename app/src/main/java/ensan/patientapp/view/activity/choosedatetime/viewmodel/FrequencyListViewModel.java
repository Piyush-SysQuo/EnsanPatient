package ensan.patientapp.view.activity.choosedatetime.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.choosedatetime.model.FrequencyResponse;
import ensan.patientapp.view.activity.choosedatetime.repository.FrequencyListRepository;


public class FrequencyListViewModel extends AndroidViewModel {

    private LiveData<FrequencyResponse> frequencyResponseLiveData;
    private FrequencyListRepository frequencyListRepository;

    public FrequencyListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        frequencyListRepository = new FrequencyListRepository();
        frequencyResponseLiveData = frequencyListRepository.frequencyResponseLiveData();
    }

    public void getFrequency(@NonNull String token) {
        frequencyListRepository.callGetFrequency(token);
    }

    public LiveData<FrequencyResponse> getVolumesResponseLiveData() {
        return frequencyResponseLiveData;
    }
}

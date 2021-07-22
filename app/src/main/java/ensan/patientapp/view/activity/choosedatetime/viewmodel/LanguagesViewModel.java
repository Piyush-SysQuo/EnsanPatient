package ensan.patientapp.view.activity.choosedatetime.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.choosedatetime.model.LanguagesResponse;
import ensan.patientapp.view.activity.choosedatetime.repository.LanguagesRepository;


public class LanguagesViewModel extends AndroidViewModel {

    private LiveData<LanguagesResponse> languagesResponseLiveData;
    private LanguagesRepository languagesRepository;

    public LanguagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        languagesRepository = new LanguagesRepository();
        languagesResponseLiveData = languagesRepository.languagesResponseLiveData();
    }

    public void getLanguages(@NonNull String token, @NonNull String language) {
        languagesRepository.callAllLanguages(token,language);
    }

    public LiveData<LanguagesResponse> getVolumesResponseLiveData() {
        return languagesResponseLiveData;
    }
}

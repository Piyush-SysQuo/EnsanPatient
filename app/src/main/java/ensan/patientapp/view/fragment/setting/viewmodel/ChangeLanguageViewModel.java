package ensan.patientapp.view.fragment.setting.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.setting.model.ChangeLanguageResp;
import ensan.patientapp.view.fragment.setting.repository.ChangeLanguageRepository;


public class ChangeLanguageViewModel extends AndroidViewModel {

    private LiveData<ChangeLanguageResp> privacySecurityRespLiveData;
    private ChangeLanguageRepository changeLanguageRepository;

    public ChangeLanguageViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        changeLanguageRepository = new ChangeLanguageRepository();
        privacySecurityRespLiveData = changeLanguageRepository.changeLanguageRespLiveData();
    }

    public void changeLanguage(@NonNull String token,@NonNull String language) {
        changeLanguageRepository.changeLanguage(token,language);
    }

    public LiveData<ChangeLanguageResp> getVolumesResponseLiveData() {
        return privacySecurityRespLiveData;
    }
}

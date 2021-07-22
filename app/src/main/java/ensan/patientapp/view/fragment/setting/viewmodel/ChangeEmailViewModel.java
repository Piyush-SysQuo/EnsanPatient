package ensan.patientapp.view.fragment.setting.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.setting.model.ChangeEmailResponse;
import ensan.patientapp.view.fragment.setting.repository.ChangeEmailRepository;


public class ChangeEmailViewModel extends AndroidViewModel {

    private LiveData<ChangeEmailResponse> changeEmailResponseLiveData;
    private ChangeEmailRepository changeEmailRepository;

    public ChangeEmailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        changeEmailRepository = new ChangeEmailRepository();
        changeEmailResponseLiveData = changeEmailRepository.changeEmailResponseLiveData();
    }

    public void changeLanguage(@NonNull String token,@NonNull String email) {
        changeEmailRepository.changeEmail(token,email);
    }

    public LiveData<ChangeEmailResponse> getVolumesResponseLiveData() {
        return changeEmailResponseLiveData;
    }
}

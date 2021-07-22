package ensan.patientapp.view.fragment.setting.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.setting.model.ChangeEmailResponse;
import ensan.patientapp.view.fragment.setting.repository.ValidateEmailRepository;


public class ValidateEmailViewModel extends AndroidViewModel {

    private LiveData<ChangeEmailResponse> changeEmailResponseLiveData;
    private ValidateEmailRepository validateEmailRepository;

    public ValidateEmailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        validateEmailRepository = new ValidateEmailRepository();
        changeEmailResponseLiveData = validateEmailRepository.changeEmailResponseLiveData();
    }

    public void changeEmail(@NonNull String token,@NonNull String email,@NonNull String otp) {
        validateEmailRepository.validateEmail(token,email,otp);
    }

    public LiveData<ChangeEmailResponse> getVolumesResponseLiveData() {
        return changeEmailResponseLiveData;
    }
}

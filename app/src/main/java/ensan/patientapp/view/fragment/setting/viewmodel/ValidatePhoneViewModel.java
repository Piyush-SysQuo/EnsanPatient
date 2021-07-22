package ensan.patientapp.view.fragment.setting.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.setting.model.ChangeEmailResponse;
import ensan.patientapp.view.fragment.setting.repository.ValidatePhoneRepository;


public class ValidatePhoneViewModel extends AndroidViewModel {

    private LiveData<ChangeEmailResponse> changeEmailResponseLiveData;
    private ValidatePhoneRepository validatePhoneRepository;

    public ValidatePhoneViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        validatePhoneRepository = new ValidatePhoneRepository();
        changeEmailResponseLiveData = validatePhoneRepository.changeEmailResponseLiveData();
    }

    public void changeLanguage(@NonNull String token,@NonNull String phone,@NonNull String otp) {
        validatePhoneRepository.validatePhone(token,phone,otp);
    }

    public LiveData<ChangeEmailResponse> getVolumesResponseLiveData() {
        return changeEmailResponseLiveData;
    }
}

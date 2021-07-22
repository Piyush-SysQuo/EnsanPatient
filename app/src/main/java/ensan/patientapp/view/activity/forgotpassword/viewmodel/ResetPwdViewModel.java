package ensan.patientapp.view.activity.forgotpassword.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.forgotpassword.model.ResetPwdResponse;
import ensan.patientapp.view.activity.forgotpassword.repository.ResetPwdRepository;

public class ResetPwdViewModel extends AndroidViewModel {

    private LiveData<ResetPwdResponse> resetPwdResponseLiveData;
    private ResetPwdRepository resetPwdRepository;

    public ResetPwdViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        resetPwdRepository = new ResetPwdRepository();
        resetPwdResponseLiveData = resetPwdRepository.resetPwdResponseLiveData();
    }

    public void restPassword(@NonNull String email, @NonNull  String password) {
        resetPwdRepository.callResetPassword(email ,password);
    }

    public LiveData<ResetPwdResponse> getVolumesResponseLiveData() {
        return resetPwdResponseLiveData;
    }
}

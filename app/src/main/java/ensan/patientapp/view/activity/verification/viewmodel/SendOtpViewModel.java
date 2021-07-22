package ensan.patientapp.view.activity.verification.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import ensan.patientapp.view.activity.verification.repository.ResendOtpRepository;


public class SendOtpViewModel extends AndroidViewModel {

    private LiveData<OtpResponse> otpResponseLiveData;
    private ResendOtpRepository resendOtpRepository;

    public SendOtpViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        resendOtpRepository = new ResendOtpRepository();
        otpResponseLiveData = resendOtpRepository.otpResponseLiveData();
    }

    public void otpVerification(@NonNull String token) {
        resendOtpRepository.resendOtpApi(token);
    }

    public LiveData<OtpResponse> getVolumesResponseLiveData() {
        return otpResponseLiveData;
    }
}

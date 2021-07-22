package ensan.patientapp.view.activity.verification.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import ensan.patientapp.view.activity.verification.repository.OtpRepository;


public class OtpViewModel extends AndroidViewModel {

    private LiveData<OtpResponse> otpResponseLiveData;
    private OtpRepository otpRepository;

    public OtpViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        otpRepository = new OtpRepository();
        otpResponseLiveData = otpRepository.otpResponseLiveData();
    }

    public void otpVerification(@NonNull String phone, String otp) {
        otpRepository.callOtpApi(phone, otp);
    }

    public LiveData<OtpResponse> getVolumesResponseLiveData() {
        return otpResponseLiveData;
    }
}

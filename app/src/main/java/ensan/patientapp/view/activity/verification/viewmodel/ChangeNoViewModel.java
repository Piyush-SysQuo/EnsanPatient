package ensan.patientapp.view.activity.verification.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import ensan.patientapp.view.activity.verification.repository.ChangeNumberRepository;


public class ChangeNoViewModel extends AndroidViewModel {

    private LiveData<OtpResponse> otpResponseLiveData;
    private ChangeNumberRepository changeNumberRepository;

    public ChangeNoViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        changeNumberRepository = new ChangeNumberRepository();
        otpResponseLiveData = changeNumberRepository.otpResponseLiveData();
    }

    public void changeNumber(@NonNull String token, @NonNull String number, @NonNull String countryCode) {
        changeNumberRepository.changeNumberApi(token,number,countryCode);
    }

    public LiveData<OtpResponse> getVolumesResponseLiveData() {
        return otpResponseLiveData;
    }
}

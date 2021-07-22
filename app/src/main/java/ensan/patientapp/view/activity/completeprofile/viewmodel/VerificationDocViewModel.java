package ensan.patientapp.view.activity.completeprofile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.completeprofile.model.VerificationDocResponse;
import ensan.patientapp.view.activity.completeprofile.repository.VerificationDocRepository;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import okhttp3.MultipartBody;

public class VerificationDocViewModel extends AndroidViewModel {

    private LiveData<LoginResponse> responseLiveData;
    private VerificationDocRepository verificationDocRepository;

    public VerificationDocViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        verificationDocRepository = new VerificationDocRepository();
        responseLiveData = verificationDocRepository.VerificationDocResponse();
    }

    public void uploadVerificationId(@NonNull String token, MultipartBody.Part profile,MultipartBody.Part[] insurance, MultipartBody.Part[] verification) {
        verificationDocRepository.callVerificationDoc(token,profile,insurance,verification);
    }

    public LiveData<LoginResponse> getVolumesResponseLiveData() {
        return responseLiveData;
    }
}

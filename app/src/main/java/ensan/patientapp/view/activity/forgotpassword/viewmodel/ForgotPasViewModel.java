package ensan.patientapp.view.activity.forgotpassword.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.forgotpassword.model.ForgotPassResponse;
import ensan.patientapp.view.activity.forgotpassword.repository.ForgotPassRepository;


public class ForgotPasViewModel extends AndroidViewModel {

    private LiveData<ForgotPassResponse> forgotPassResponseLiveData;
    private ForgotPassRepository forgotPassRepository;

    public ForgotPasViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        forgotPassRepository = new ForgotPassRepository();
        forgotPassResponseLiveData = forgotPassRepository.forgotPassResponseLiveData();
    }

    public void forgotPassword(@NonNull String email, @NonNull String language) {
        forgotPassRepository.callForgotPassApi(email,language);
    }

    public LiveData<ForgotPassResponse> getVolumesResponseLiveData() {
        return forgotPassResponseLiveData;
    }
}

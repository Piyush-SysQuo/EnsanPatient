package ensan.patientapp.view.activity.login.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.login.repository.LogInRepository;


public class LogInViewModel extends AndroidViewModel {

    private LiveData<LoginResponse> logInResponseMutableLiveData;
    private LogInRepository loginRepository;

    public LogInViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        loginRepository = new LogInRepository();
        logInResponseMutableLiveData = loginRepository.logInResponseLiveData();
    }

    public void userLogin(@NonNull String email, @NonNull  String password, @NonNull String deviceToken, @NonNull String language) {
        loginRepository.callLoginApi(email ,password,deviceToken,language);
    }

    public LiveData<LoginResponse> getVolumesResponseLiveData() {
        return logInResponseMutableLiveData;
    }
}

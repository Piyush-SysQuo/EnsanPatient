package ensan.patientapp.view.activity.signup.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.signup.model.SignUpResponse;
import ensan.patientapp.view.activity.signup.repository.SignUpRepository;


public class SignUpViewModel extends AndroidViewModel {

    private LiveData<SignUpResponse> signUpResponseLiveData;
    private SignUpRepository signUpRepository;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        signUpRepository = new SignUpRepository();
        signUpResponseLiveData = signUpRepository.signUpResponseLiveData();
    }

    public void userSignUp(@NonNull String fName, @NonNull String famName, @NonNull String mobileNo, @NonNull String email, @NonNull  String password
    ,@NonNull String role, @NonNull String dob, @NonNull  String language ,@NonNull  String iqmaId,@NonNull  String countryCode) {
        signUpRepository.callRegisterApi(fName,famName,mobileNo,email,password,role,dob,language,iqmaId,countryCode);
    }

    public LiveData<SignUpResponse> getVolumesResponseLiveData() {
        return signUpResponseLiveData;
    }
}

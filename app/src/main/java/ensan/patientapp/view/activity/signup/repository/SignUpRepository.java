package ensan.patientapp.view.activity.signup.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.signup.model.SignUpResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository {

    private MutableLiveData<SignUpResponse> mutableLiveData;


    public SignUpRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call user register api
    public void callRegisterApi(@NonNull String fName, @NonNull String famName, @NonNull String mobileNo, @NonNull String email, @NonNull  String password
            ,@NonNull String role, @NonNull String dob, @NonNull  String language ,@NonNull  String iqmaId,@NonNull  String countryCode){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SignUpResponse> call = apiDataService.signUpApi("application/json",fName,famName,email,mobileNo,password,role,dob,language,iqmaId,countryCode);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body() != null) {
                 mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SignUpResponse> signUpResponseLiveData() {
        return mutableLiveData;
    }


}

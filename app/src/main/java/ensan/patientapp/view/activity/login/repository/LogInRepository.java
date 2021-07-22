package ensan.patientapp.view.activity.login.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.Utils.Constants;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInRepository {

    private MutableLiveData<LoginResponse> mutableLiveData;


    public LogInRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call login api
    public void callLoginApi(@NonNull String email , @NonNull String password, @NonNull String deviceToken, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<LoginResponse> call = apiDataService.GetValidLogin("application/json",email,password,deviceToken,language, Constants.KEY_PATIENT_ROLE);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<LoginResponse> logInResponseLiveData() {
        return mutableLiveData;
    }


}

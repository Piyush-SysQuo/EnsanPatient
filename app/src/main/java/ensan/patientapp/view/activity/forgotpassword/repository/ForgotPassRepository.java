package ensan.patientapp.view.activity.forgotpassword.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.Utils.Constants;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.forgotpassword.model.ForgotPassResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassRepository {

    private MutableLiveData<ForgotPassResponse> mutableLiveData;


    public ForgotPassRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call forgot password api
    public void callForgotPassApi(@NonNull String email, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ForgotPassResponse> call = apiDataService.forgotPassword("application/json",email,language, Constants.KEY_PATIENT_ROLE);
        call.enqueue(new Callback<ForgotPassResponse>() {
            @Override
            public void onResponse(Call<ForgotPassResponse> call, Response<ForgotPassResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ForgotPassResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ForgotPassResponse> forgotPassResponseLiveData() {
        return mutableLiveData;
    }


}

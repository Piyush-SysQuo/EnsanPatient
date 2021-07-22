package ensan.patientapp.view.activity.forgotpassword.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.forgotpassword.model.ResetPwdResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPwdRepository {

    private MutableLiveData<ResetPwdResponse> mutableLiveData;


    public ResetPwdRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call login api
    public void callResetPassword(@NonNull String email , @NonNull String password){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ResetPwdResponse> call = apiDataService.resetPassword("application/json",email,password);
        call.enqueue(new Callback<ResetPwdResponse>() {
            @Override
            public void onResponse(Call<ResetPwdResponse> call, Response<ResetPwdResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResetPwdResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ResetPwdResponse> resetPwdResponseLiveData() {
        return mutableLiveData;
    }


}

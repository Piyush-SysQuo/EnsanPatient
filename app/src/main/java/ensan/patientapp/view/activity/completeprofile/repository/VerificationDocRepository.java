package ensan.patientapp.view.activity.completeprofile.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.model.VerificationDocResponse;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationDocRepository {

    private MutableLiveData<LoginResponse> mutableLiveData;


    public VerificationDocRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call verification doc API
    public void callVerificationDoc(@NonNull String token, MultipartBody.Part profile, MultipartBody.Part[] insurance, MultipartBody.Part[] verification){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<LoginResponse> call = apiDataService.saveDocs("application/json","Bearer "+token,insurance,verification,profile);
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

    public LiveData<LoginResponse> VerificationDocResponse() {
        return mutableLiveData;
    }


}

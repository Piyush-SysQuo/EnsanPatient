package ensan.patientapp.view.activity.privacypolicy.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.privacypolicy.model.PrivacySecurityResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyRepository {

    private MutableLiveData<PrivacySecurityResp> mutableLiveData;


    public PrivacyPolicyRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get about app api
    public void getPrivacy(@NonNull String token, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<PrivacySecurityResp> call = apiDataService.getPrivacyText("application/json","Bearer "+token,language);
        call.enqueue(new Callback<PrivacySecurityResp>() {
            @Override
            public void onResponse(Call<PrivacySecurityResp> call, Response<PrivacySecurityResp> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<PrivacySecurityResp> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<PrivacySecurityResp> privacySecurityRespLiveData() {
        return mutableLiveData;
    }


}

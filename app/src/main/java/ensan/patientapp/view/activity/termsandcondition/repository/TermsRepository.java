package ensan.patientapp.view.activity.termsandcondition.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.privacypolicy.model.PrivacySecurityResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsRepository {

    private MutableLiveData<PrivacySecurityResp> mutableLiveData;


    public TermsRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get terms api
    public void getTerms(@NonNull String token, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<PrivacySecurityResp> call = apiDataService.getTermsText("application/json","Bearer "+token,language);
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

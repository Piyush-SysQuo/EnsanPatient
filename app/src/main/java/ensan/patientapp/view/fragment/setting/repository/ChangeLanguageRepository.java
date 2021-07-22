package ensan.patientapp.view.fragment.setting.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.setting.model.ChangeLanguageResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeLanguageRepository {

    private MutableLiveData<ChangeLanguageResp> mutableLiveData;


    public ChangeLanguageRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call change language
    public void changeLanguage(@NonNull String token, @NonNull String languageCode){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ChangeLanguageResp> call = apiDataService.setLanguage("application/json","Bearer "+token,languageCode);
        call.enqueue(new Callback<ChangeLanguageResp>() {
            @Override
            public void onResponse(Call<ChangeLanguageResp> call, Response<ChangeLanguageResp> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChangeLanguageResp> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ChangeLanguageResp> changeLanguageRespLiveData() {
        return mutableLiveData;
    }


}

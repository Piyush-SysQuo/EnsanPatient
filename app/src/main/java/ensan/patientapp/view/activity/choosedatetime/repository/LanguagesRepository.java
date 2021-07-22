package ensan.patientapp.view.activity.choosedatetime.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.choosedatetime.model.LanguagesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguagesRepository {

    private MutableLiveData<LanguagesResponse> mutableLiveData;


    public LanguagesRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call languages list api
    public void callAllLanguages(@NonNull String token, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<LanguagesResponse> call = apiDataService.getAllLanguages("application/json","Bearer "+token,language);
        call.enqueue(new Callback<LanguagesResponse>() {
            @Override
            public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LanguagesResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<LanguagesResponse> languagesResponseLiveData() {
        return mutableLiveData;
    }


}

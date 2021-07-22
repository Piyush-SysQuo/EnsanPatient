package ensan.patientapp.view.fragment.about.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.about.model.AboutResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutRepository {

    private MutableLiveData<AboutResponse> mutableLiveData;


    public AboutRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get about app api
    public void getAbout(@NonNull String token, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<AboutResponse> call = apiDataService.getAboutText("application/json","Bearer "+token,language);
        call.enqueue(new Callback<AboutResponse>() {
            @Override
            public void onResponse(Call<AboutResponse> call, Response<AboutResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AboutResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<AboutResponse> aboutResponseLiveData() {
        return mutableLiveData;
    }


}

package ensan.patientapp.view.activity.choosedatetime.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.choosedatetime.model.FrequencyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrequencyListRepository {

    private MutableLiveData<FrequencyResponse> mutableLiveData;


    public FrequencyListRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call frequency  list api
    public void callGetFrequency(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<FrequencyResponse> call = apiDataService.getFrequency("application/json","Bearer "+token);
        call.enqueue(new Callback<FrequencyResponse>() {
            @Override
            public void onResponse(Call<FrequencyResponse> call, Response<FrequencyResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<FrequencyResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<FrequencyResponse> frequencyResponseLiveData() {
        return mutableLiveData;
    }


}

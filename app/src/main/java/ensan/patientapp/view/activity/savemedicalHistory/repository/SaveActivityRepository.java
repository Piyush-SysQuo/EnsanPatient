package ensan.patientapp.view.activity.savemedicalHistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.savemedicalHistory.model.ActivityResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveActivityRepository {

    private MutableLiveData<ActivityResponse> mutableLiveData;


    public SaveActivityRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call activity api
    public void saveActivity(@NonNull String token, @NonNull String smoke, @NonNull String exercise, @NonNull String familyId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ActivityResponse> call = apiDataService.addActivity("application/json","Bearer "+token,smoke,exercise,familyId);
        call.enqueue(new Callback<ActivityResponse>() {
            @Override
            public void onResponse(Call<ActivityResponse> call, Response<ActivityResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActivityResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ActivityResponse> activityResponseLiveData() {
        return mutableLiveData;
    }


}

package ensan.patientapp.view.activity.caregiverprofile.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.caregiverprofile.model.CareGiverProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CareGiverProfileRepository {

    private MutableLiveData<CareGiverProfileResponse> mutableLiveData;


    public CareGiverProfileRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get caregiver profile api
    public void getCareGiverProfile(@NonNull String token, @NonNull String careGiverID){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CareGiverProfileResponse> call = apiDataService.getCareGiverProfile("application/json","Bearer "+token,careGiverID);
        call.enqueue(new Callback<CareGiverProfileResponse>() {
            @Override
            public void onResponse(Call<CareGiverProfileResponse> call, Response<CareGiverProfileResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CareGiverProfileResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CareGiverProfileResponse> careGiverProfileResponseLiveData() {
        return mutableLiveData;
    }


}

package ensan.patientapp.view.activity.editmedicalhistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.editmedicalhistory.model.UpdateProfileInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileInfoRepository {

    private MutableLiveData<UpdateProfileInfoResponse> mutableLiveData;


    public UpdateProfileInfoRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call updateProfileInfo API
    public void updateProfileInfo(@NonNull String token, @NonNull String bloodGroup, @NonNull String height, @NonNull String weight){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<UpdateProfileInfoResponse> call = apiDataService.updateProfileInfo("application/json","Bearer "+token,bloodGroup,height,weight);
        call.enqueue(new Callback<UpdateProfileInfoResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileInfoResponse> call, Response<UpdateProfileInfoResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileInfoResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<UpdateProfileInfoResponse> updateProfileInfoResponseLiveData() {
        return mutableLiveData;
    }


}

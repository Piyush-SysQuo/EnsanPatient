package ensan.patientapp.view.activity.completeprofile.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteFirstProfileRepository {

    private MutableLiveData<ProfileResponse> mutableLiveData;


    public CompleteFirstProfileRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call complete first profile api
    public void callFirstProfilerApi(@NonNull String token, @NonNull String gender, @NonNull String nationality, @NonNull String maritalStatus){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ProfileResponse> call = apiDataService.completeFirstProfile("application/json","Bearer "+token,gender,nationality,maritalStatus);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                 mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ProfileResponse> profileResponseLiveData() {
        return mutableLiveData;
    }


}

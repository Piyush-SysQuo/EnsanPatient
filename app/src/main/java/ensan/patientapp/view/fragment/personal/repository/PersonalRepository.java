package ensan.patientapp.view.fragment.personal.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.personal.model.UserProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalRepository {

    private MutableLiveData<UserProfileResponse> mutableLiveData;


    public PersonalRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get user detail api
    public void callGetUserDetail(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<UserProfileResponse> call = apiDataService.getUserDetails("application/json","Bearer "+token);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<UserProfileResponse> detailResponseLiveData() {
        return mutableLiveData;
    }


}

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

public class CompleteSecondProfileRepository {

    private MutableLiveData<ProfileResponse> mutableLiveData;


    public CompleteSecondProfileRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call second first profile api
    public void callSecondProfilerApi(@NonNull String token, @NonNull String child, @NonNull String height, @NonNull String weight, @NonNull String b_group, @NonNull String emergency_number, @NonNull String family_member_id){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ProfileResponse> call = apiDataService.completeSecondProfile("application/json","Bearer "+token,child,height,weight,b_group,emergency_number);
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

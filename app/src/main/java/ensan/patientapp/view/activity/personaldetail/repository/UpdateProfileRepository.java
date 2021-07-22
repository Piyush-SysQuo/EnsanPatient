package ensan.patientapp.view.activity.personaldetail.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.personaldetail.model.UpdateProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileRepository {

    private MutableLiveData<UpdateProfileResponse> mutableLiveData;


    public UpdateProfileRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call allergy api
    public void updateProfile(@NonNull String token, @NonNull String email, @NonNull String phone, @NonNull String maritalStatus, @NonNull String emergencyNo, @NonNull String childern, @NonNull String gender , @NonNull String nationality){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<UpdateProfileResponse> call = apiDataService.updateProfile("application/json","Bearer "+token,email,phone,maritalStatus,emergencyNo,childern, gender, nationality);
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<UpdateProfileResponse> updateProfileResponseLiveData() {
        return mutableLiveData;
    }


}

package ensan.patientapp.view.fragment.personal.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.personal.model.ProfilePicResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePicRepository {

    private MutableLiveData<ProfilePicResponse> mutableLiveData;


    public ProfilePicRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call  update profile pic API
    public void callUpdateProfilePic(@NonNull String token, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ProfilePicResponse> call = apiDataService.updateProfilePic("application/json","Bearer "+token,part);
        call.enqueue(new Callback<ProfilePicResponse>() {
            @Override
            public void onResponse(Call<ProfilePicResponse> call, Response<ProfilePicResponse> response) {
                if (response.body() != null) {
                  mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProfilePicResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ProfilePicResponse> profileResponseLiveData() {
        return mutableLiveData;
    }


}

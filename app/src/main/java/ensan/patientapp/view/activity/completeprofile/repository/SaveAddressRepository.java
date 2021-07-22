package ensan.patientapp.view.activity.completeprofile.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveAddressRepository {

    private MutableLiveData<ProfileResponse> mutableLiveData;


    public SaveAddressRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call save address api
    public void saveAddress(@NonNull String token, @NonNull RequestBody address, @NonNull RequestBody poBox, @NonNull RequestBody city , @NonNull RequestBody country, @NonNull RequestBody familyId, @NonNull RequestBody lat, @NonNull RequestBody lng, @NonNull RequestBody tag){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ProfileResponse> call = apiDataService.saveAddress("application/json","Bearer "+token,address,poBox,city,country,familyId,lng,lat,tag);
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

package ensan.patientapp.view.fragment.personal.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.personal.model.CoverPicResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoverPicRepository {

    private MutableLiveData<CoverPicResponse> mutableLiveData;


    public CoverPicRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call  update cover pic API
    public void callUpdateCoverPic(@NonNull String token, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CoverPicResponse> call = apiDataService.updateCoverPic("application/json","Bearer "+token,part);
        call.enqueue(new Callback<CoverPicResponse>() {
            @Override
            public void onResponse(Call<CoverPicResponse> call, Response<CoverPicResponse> response) {
                if (response.body() != null) {
                 mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CoverPicResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CoverPicResponse> profileResponseLiveData() {
        return mutableLiveData;
    }


}

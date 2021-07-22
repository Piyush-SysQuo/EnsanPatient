package ensan.patientapp.view.activity.completeprofile.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.model.InsuranceDocResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceRepository {

    private MutableLiveData<InsuranceDocResponse> mutableLiveData;


    public InsuranceRepository() {

        mutableLiveData = new MutableLiveData<>();
    }
    // call insurance API
    public void callUploadInsuranceDoc(@NonNull String token, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<InsuranceDocResponse> call = apiDataService.uploadInsurancePic("application/json","Bearer "+token,part);
        call.enqueue(new Callback<InsuranceDocResponse>() {
            @Override
            public void onResponse(Call<InsuranceDocResponse> call, Response<InsuranceDocResponse> response) {
                if (response.body() != null) {
                 mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<InsuranceDocResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<InsuranceDocResponse> insuranceDocResponseLiveData() {
        return mutableLiveData;
    }


}

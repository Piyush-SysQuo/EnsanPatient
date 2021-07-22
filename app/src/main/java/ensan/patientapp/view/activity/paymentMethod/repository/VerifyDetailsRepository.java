package ensan.patientapp.view.activity.paymentMethod.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.paymentMethod.model.VerifyDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyDetailsRepository {

    private MutableLiveData<VerifyDetailsResponse> mutableLiveData;


    public VerifyDetailsRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // verify card details API
    public void getVerifyCardDetails(@NonNull String token,@NonNull String resourcePath,  @NonNull String cardType){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<VerifyDetailsResponse> call = apiDataService.verifyCardDetail("application/x-www-form-urlencoded","Bearer "+token,resourcePath,cardType);
        call.enqueue(new Callback<VerifyDetailsResponse>() {
            @Override
            public void onResponse(Call<VerifyDetailsResponse> call, Response<VerifyDetailsResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VerifyDetailsResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<VerifyDetailsResponse> checkOutIdResponseLiveData() {
        return mutableLiveData;
    }


}

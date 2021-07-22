package ensan.patientapp.view.activity.verification.repository;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNumberRepository {

    private MutableLiveData<OtpResponse> mutableLiveData;


    public ChangeNumberRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // resend otp verification api
    public void changeNumberApi(@NonNull String token, @NonNull String number, @NonNull String countryCode){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<OtpResponse> call = apiDataService.changeNumber("application/json","Bearer "+token,number,countryCode);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.body() != null) {
                 mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<OtpResponse> otpResponseLiveData() {
        return mutableLiveData;
    }


}

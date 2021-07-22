package ensan.patientapp.view.activity.paymentMethod.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.paymentMethod.model.CheckOutIdResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    private MutableLiveData<CheckOutIdResponse> mutableLiveData;


    public PaymentRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    //  payment api
    public void doPayment(@NonNull String token, @NonNull String amount, @NonNull String cardTokenId,@NonNull String bookingId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CheckOutIdResponse> call = apiDataService.doPayment("application/json","Bearer "+token,amount,cardTokenId,bookingId);
        call.enqueue(new Callback<CheckOutIdResponse>() {
            @Override
            public void onResponse(Call<CheckOutIdResponse> call, Response<CheckOutIdResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CheckOutIdResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CheckOutIdResponse> doPaymentLiveData() {
        return mutableLiveData;
    }


}

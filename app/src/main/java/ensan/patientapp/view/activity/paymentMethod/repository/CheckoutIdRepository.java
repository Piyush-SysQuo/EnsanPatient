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

public class CheckoutIdRepository {

    private MutableLiveData<CheckOutIdResponse> mutableLiveData;


    public CheckoutIdRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // get payment checkout id
    public void getCheckOutId(@NonNull String token, String cardType){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CheckOutIdResponse> call = apiDataService.getCheckoutId("application/json","Bearer "+token,cardType);
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

    public LiveData<CheckOutIdResponse> checkOutIdResponseLiveData() {
        return mutableLiveData;
    }


}

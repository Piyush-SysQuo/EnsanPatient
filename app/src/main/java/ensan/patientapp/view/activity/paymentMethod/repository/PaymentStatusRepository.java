package ensan.patientapp.view.activity.paymentMethod.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.paymentMethod.model.GetSaveCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentStatusRepository {

    private MutableLiveData<GetSaveCard> mutableLiveData;


    public PaymentStatusRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    //  payment status api
    public void paymentStatus(@NonNull String token, @NonNull String resourcePath, @NonNull String bookingId, @NonNull String cardToken, @NonNull String bookingFor){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetSaveCard> call = apiDataService.getPaymentStatus("application/json","Bearer "+token,resourcePath,bookingId,cardToken,bookingFor);
        call.enqueue(new Callback<GetSaveCard>() {
            @Override
            public void onResponse(Call<GetSaveCard> call, Response<GetSaveCard> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetSaveCard> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<GetSaveCard> doPaymentLiveData() {
        return mutableLiveData;
    }


}

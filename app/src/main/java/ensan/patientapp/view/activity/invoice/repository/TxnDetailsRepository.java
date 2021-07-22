package ensan.patientapp.view.activity.invoice.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.invoice.model.TransactionDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TxnDetailsRepository {

    private MutableLiveData<TransactionDetailsResponse> mutableLiveData;


    public TxnDetailsRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void callTxnDetails(@NonNull String token, @NonNull String bookingId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<TransactionDetailsResponse> call = apiDataService.getTransactionDetails("x-www-form-urlencoded","Bearer "+token,bookingId);
        call.enqueue(new Callback<TransactionDetailsResponse>() {
            @Override
            public void onResponse(Call<TransactionDetailsResponse> call, Response<TransactionDetailsResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<TransactionDetailsResponse> call, Throwable t) {
                call.cancel();
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<TransactionDetailsResponse> transactionDetailsResponseLiveData() {
        return mutableLiveData;
    }


}

package ensan.patientapp.view.activity.refundpolicy.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.refundpolicy.model.RefundPolicyResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundPolicyRepository {

    private MutableLiveData<RefundPolicyResp> mutableLiveData;


    public RefundPolicyRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get refundPolicy api
    public void getRefundPolicy(@NonNull String token, @NonNull String language){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<RefundPolicyResp> call = apiDataService.refundPolicyText("application/json","Bearer "+token,language);
        call.enqueue(new Callback<RefundPolicyResp>() {
            @Override
            public void onResponse(Call<RefundPolicyResp> call, Response<RefundPolicyResp> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RefundPolicyResp> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<RefundPolicyResp> refundPolicyRespLiveData() {
        return mutableLiveData;
    }


}

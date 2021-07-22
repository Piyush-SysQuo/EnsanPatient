package ensan.patientapp.view.fragment.addresses.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.addresses.model.SaveAddressResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDefaultAddRepository {

    private MutableLiveData<SaveAddressResponse> mutableLiveData;


    public SetDefaultAddRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // set address as default
    public void setDefaultAddress(@NonNull String token , @NonNull String addressId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SaveAddressResponse> call = apiDataService.setAddressDefault("application/json","Bearer "+token,addressId);
        call.enqueue(new Callback<SaveAddressResponse>() {
            @Override
            public void onResponse(Call<SaveAddressResponse> call, Response<SaveAddressResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SaveAddressResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SaveAddressResponse> addressResponseLiveData() {
        return mutableLiveData;
    }


}

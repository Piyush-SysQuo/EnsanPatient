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

public class SaveNewAddressRepository {



    private MutableLiveData<SaveAddressResponse> mutableLiveData;


    public SaveNewAddressRepository() {

        mutableLiveData = new MutableLiveData<>();
    }


    // call save address api
    public void saveAddress(@NonNull String token,@NonNull String familyId, @NonNull String address, @NonNull String poBox, @NonNull String city , @NonNull String country,  @NonNull String lat, @NonNull String lng, @NonNull String landmark,@NonNull String primaryadd,@NonNull String addmore,@NonNull String tag){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SaveAddressResponse> call = apiDataService.saveNewAddress("application/json","Bearer "+token,familyId,address,poBox,city,country,lng,lat,landmark,primaryadd,addmore,tag);
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

    public LiveData<SaveAddressResponse> saveAddressResponseLiveData() {
        return mutableLiveData;
    }

}

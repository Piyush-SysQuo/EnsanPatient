package ensan.patientapp.view.fragment.addresses.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.addresses.model.DeleteAddressResponse;
import ensan.patientapp.view.fragment.addresses.model.GetAddressesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressesRepository {



    private MutableLiveData<GetAddressesResponse> mutableLiveData;
    private MutableLiveData<DeleteAddressResponse> mutableLiveData2;


    public AddressesRepository() {

        mutableLiveData = new MutableLiveData<>();
        mutableLiveData2=new MutableLiveData<>();
    }

    // call my schedule api
    public void callGetAddressApi(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetAddressesResponse> call = apiDataService.getAddresses("application/json","Bearer "+token);
        call.enqueue(new Callback<GetAddressesResponse>() {
            @Override
            public void onResponse(Call<GetAddressesResponse> call, Response<GetAddressesResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetAddressesResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }


    public void callDeleteAddress(@NonNull String token,int id){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<DeleteAddressResponse> call = apiDataService.deleteAddress("application/json","Bearer "+token,id);
        call.enqueue(new Callback<DeleteAddressResponse>() {
            @Override
            public void onResponse(Call<DeleteAddressResponse> call, Response<DeleteAddressResponse> response) {
                if (response.body() != null) {
                    mutableLiveData2.postValue(response.body());
                }else{
                    mutableLiveData2.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DeleteAddressResponse> call, Throwable t) {
                mutableLiveData2.postValue(null);
            }
        });
    }

    public LiveData<GetAddressesResponse> getAddresessResponseLiveData() {
        return mutableLiveData;
    }

    public LiveData<DeleteAddressResponse> deleteAddressResponseLiveData() { return mutableLiveData2;}

}

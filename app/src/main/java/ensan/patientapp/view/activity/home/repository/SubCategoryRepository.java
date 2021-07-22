package ensan.patientapp.view.activity.home.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.home.model.SubCategoryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryRepository {

    private MutableLiveData<SubCategoryResponse> mutableLiveData;


    public SubCategoryRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get sub category api
    public void getCategoryData(@NonNull String token, @NonNull String categoryId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SubCategoryResponse> call = apiDataService.getSubCategory("application/json","Bearer "+token, categoryId);
        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SubCategoryResponse> subCategoryResponseLiveData() {
        return mutableLiveData;
    }


}

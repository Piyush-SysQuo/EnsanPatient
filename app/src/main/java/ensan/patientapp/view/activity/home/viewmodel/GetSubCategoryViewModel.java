package ensan.patientapp.view.activity.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.home.model.SubCategoryResponse;
import ensan.patientapp.view.activity.home.repository.SubCategoryRepository;

public class GetSubCategoryViewModel extends AndroidViewModel {

    private LiveData<SubCategoryResponse> subCategoryResponseLiveData;
    private SubCategoryRepository subCategoryRepository;

    public GetSubCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        subCategoryRepository = new SubCategoryRepository();
        subCategoryResponseLiveData = subCategoryRepository.subCategoryResponseLiveData();
    }

    public void getSubCategory(@NonNull String token,@NonNull String categoryID) {
        subCategoryRepository.getCategoryData(token,categoryID);
    }

    public LiveData<SubCategoryResponse> getVolumesResponseLiveData() {
        return subCategoryResponseLiveData;
    }
}

package ensan.patientapp.view.activity.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.home.model.CategoryResponse;
import ensan.patientapp.view.activity.home.repository.CategoryRepository;


public class GetCategoryViewModel extends AndroidViewModel {

    private LiveData<CategoryResponse> categoryResponseLiveData;
    private CategoryRepository categoryRepository;

    public GetCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        categoryRepository = new CategoryRepository();
        categoryResponseLiveData = categoryRepository.categoryResponseLiveData();
    }

    public void getCategory(@NonNull String token) {
        categoryRepository.getCategoryData(token);
    }

    public LiveData<CategoryResponse> getVolumesResponseLiveData() {
        return categoryResponseLiveData;
    }
}

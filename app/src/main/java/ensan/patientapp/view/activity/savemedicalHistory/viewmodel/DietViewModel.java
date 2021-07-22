package ensan.patientapp.view.activity.savemedicalHistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;

import ensan.patientapp.view.activity.savemedicalHistory.model.DietResponse;
import ensan.patientapp.view.activity.savemedicalHistory.repository.SaveDietRepository;


public class DietViewModel extends AndroidViewModel {

    private LiveData<DietResponse> dietResponseLiveData;
    private SaveDietRepository saveDietRepository;

    public DietViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        saveDietRepository = new SaveDietRepository();
        dietResponseLiveData = saveDietRepository.dietResponseLiveData();
    }

    public void saveDiet(@NonNull String token, @NonNull JSONArray jsonArray, @NonNull String familyId) {
        saveDietRepository.saveDiet(token,jsonArray,familyId);
    }

    public LiveData<DietResponse> getVolumesResponseLiveData() {
        return dietResponseLiveData;
    }
}

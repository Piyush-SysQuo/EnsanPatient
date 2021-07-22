package ensan.patientapp.view.activity.savemedicalHistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.savemedicalHistory.model.AllergyResponse;
import ensan.patientapp.view.activity.savemedicalHistory.repository.AllergyRepository;


public class AllergyViewModel extends AndroidViewModel {

    private LiveData<AllergyResponse> allergyResponseLiveData;
    private AllergyRepository allergyRepository;

    public AllergyViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        allergyRepository = new AllergyRepository();
        allergyResponseLiveData = allergyRepository.allergyResponseLiveData();
    }

    public void saveAllergy(@NonNull String token, @NonNull String smoke, @NonNull String exercise,String familyId) {
        allergyRepository.saveAllergy(token,smoke,exercise,familyId);
    }

    public LiveData<AllergyResponse> getVolumesResponseLiveData() {
        return allergyResponseLiveData;
    }
}

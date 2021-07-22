package ensan.patientapp.view.activity.caregiverprofile.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.caregiverprofile.model.CareGiverProfileResponse;
import ensan.patientapp.view.activity.caregiverprofile.repository.CareGiverProfileRepository;

public class CareGiverProfileViewModel extends AndroidViewModel {

    private LiveData<CareGiverProfileResponse> careGiverProfileResponseLiveData;
    private CareGiverProfileRepository careGiverProfileRepository;

    public CareGiverProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        careGiverProfileRepository = new CareGiverProfileRepository();
        careGiverProfileResponseLiveData = careGiverProfileRepository.careGiverProfileResponseLiveData();
    }

    public void getCareGiverProfile(@NonNull String token, @NonNull String careGiverId) {
        careGiverProfileRepository.getCareGiverProfile(token,careGiverId);
    }

    public LiveData<CareGiverProfileResponse> getVolumesResponseLiveData() {
        return careGiverProfileResponseLiveData;
    }
}

package ensan.patientapp.view.activity.medicalhistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.medicalhistory.reppsitory.MedicalHistoryRepository;

public class MedicalHistoryViewModel extends AndroidViewModel {

    private LiveData<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> medicalHistoryResponseLiveData;
    private MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        medicalHistoryRepository = new MedicalHistoryRepository();
        medicalHistoryResponseLiveData = medicalHistoryRepository.medicalHistoryResponseLiveData();
    }

    public void getMedicalHistory(@NonNull String token, @NonNull String familyId) {
        medicalHistoryRepository.getMedicalHistory(token,familyId);
    }

    public LiveData<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> getVolumesResponseLiveData() {
        return medicalHistoryResponseLiveData;
    }
}

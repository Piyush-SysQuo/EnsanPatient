package ensan.patientapp.view.activity.savemedicalHistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.savemedicalHistory.model.MedicalHistoryResponse;
import ensan.patientapp.view.activity.savemedicalHistory.repository.SaveMedicalHistoryRepository;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MedicalHistoryViewModel extends AndroidViewModel {

    private LiveData<MedicalHistoryResponse> profileResponseLiveData;
    private SaveMedicalHistoryRepository saveMedicalHistoryRepository;

    public MedicalHistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        saveMedicalHistoryRepository = new SaveMedicalHistoryRepository();
        profileResponseLiveData = saveMedicalHistoryRepository.medicalHistoryResponseLiveData();
    }

    public void saveMedicalHistoryAddress(@NonNull String token, @NonNull RequestBody medicalKey, @NonNull RequestBody description, MultipartBody.Part part,@NonNull RequestBody id, @NonNull RequestBody family) {
        saveMedicalHistoryRepository.saveMedicalHistoryAddress(token,medicalKey,description,part,id,family);
    }

    public LiveData<MedicalHistoryResponse> getVolumesResponseLiveData() {
        return profileResponseLiveData;
    }
}

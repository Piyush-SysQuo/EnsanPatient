package ensan.patientapp.view.activity.idProof.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import ensan.patientapp.view.activity.idProof.repository.IdProofRepository;


public class IdProofViewModel extends AndroidViewModel {

    private LiveData<IdProofResponse> insuranceResponseLiveData;
    private IdProofRepository idProofRepository;

    public IdProofViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        idProofRepository = new IdProofRepository();
       insuranceResponseLiveData = idProofRepository.idProofResponseLiveData();
    }

    public void getIdProof(@NonNull String token) {
        idProofRepository.callGetIdProof(token);
    }

    public LiveData<IdProofResponse> getVolumesResponseLiveData() {
        return insuranceResponseLiveData;
    }
}

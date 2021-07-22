package ensan.patientapp.view.activity.idinsurance.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import ensan.patientapp.view.activity.idinsurance.repository.InsuranceIdRepository;


public class InsuranceViewModel extends AndroidViewModel {

    private LiveData<InsuranceResponse> detailResponseLiveData;
    private InsuranceIdRepository insuranceIdRepository;

    public InsuranceViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        insuranceIdRepository = new InsuranceIdRepository();
        detailResponseLiveData = insuranceIdRepository.idProofResponseLiveData();
    }

    public void getInsuranceCard(@NonNull String token) {
        insuranceIdRepository.callGetInsuranceCard(token);
    }

    public LiveData<InsuranceResponse> getVolumesResponseLiveData() {
        return detailResponseLiveData;
    }
}

package ensan.patientapp.view.activity.completeprofile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.completeprofile.model.InsuranceDocResponse;
import ensan.patientapp.view.activity.completeprofile.repository.InsuranceRepository;
import okhttp3.MultipartBody;

public class InsuranceDocViewModel extends AndroidViewModel {

    private LiveData<InsuranceDocResponse> responseLiveData;
    private InsuranceRepository insuranceRepository;

    public InsuranceDocViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        insuranceRepository = new InsuranceRepository();
        responseLiveData = insuranceRepository.insuranceDocResponseLiveData();
    }

    public void uploadInsurancePic(@NonNull String token, MultipartBody.Part part) {
        insuranceRepository.callUploadInsuranceDoc(token,part);
    }

    public LiveData<InsuranceDocResponse> getVolumesResponseLiveData() {
        return responseLiveData;
    }
}

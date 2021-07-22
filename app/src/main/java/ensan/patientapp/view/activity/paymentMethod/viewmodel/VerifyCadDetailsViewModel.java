package ensan.patientapp.view.activity.paymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.paymentMethod.model.VerifyDetailsResponse;
import ensan.patientapp.view.activity.paymentMethod.repository.VerifyDetailsRepository;


public class VerifyCadDetailsViewModel extends AndroidViewModel {

    private LiveData<VerifyDetailsResponse> checkOutIdResponseLiveData;
    private VerifyDetailsRepository verifyDetailsRepository;

    public VerifyCadDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        verifyDetailsRepository = new VerifyDetailsRepository();
        checkOutIdResponseLiveData = verifyDetailsRepository.checkOutIdResponseLiveData();
    }

    public void verifyDetails(@NonNull String token, @NonNull String resourceId, @NonNull String cardType) {
        verifyDetailsRepository.getVerifyCardDetails(token,resourceId,cardType);
    }

    public LiveData<VerifyDetailsResponse> getVolumesResponseLiveData() {
        return checkOutIdResponseLiveData;
    }
}

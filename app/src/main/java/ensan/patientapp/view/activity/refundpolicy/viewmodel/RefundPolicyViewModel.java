package ensan.patientapp.view.activity.refundpolicy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.refundpolicy.model.RefundPolicyResp;
import ensan.patientapp.view.activity.refundpolicy.repository.RefundPolicyRepository;


public class RefundPolicyViewModel extends AndroidViewModel {

    private LiveData<RefundPolicyResp> privacySecurityRespLiveData;
    private RefundPolicyRepository refundPolicyRepository;

    public RefundPolicyViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        refundPolicyRepository = new RefundPolicyRepository();
        privacySecurityRespLiveData = refundPolicyRepository.refundPolicyRespLiveData();
    }

    public void getRefundPolicy(@NonNull String token,@NonNull String language) {
        refundPolicyRepository.getRefundPolicy(token,language);
    }

    public LiveData<RefundPolicyResp> getVolumesResponseLiveData() {
        return privacySecurityRespLiveData;
    }
}

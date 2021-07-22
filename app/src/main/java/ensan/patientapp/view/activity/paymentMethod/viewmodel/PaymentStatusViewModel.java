package ensan.patientapp.view.activity.paymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.paymentMethod.model.GetSaveCard;
import ensan.patientapp.view.activity.paymentMethod.repository.PaymentStatusRepository;


public class PaymentStatusViewModel extends AndroidViewModel {

    private LiveData<GetSaveCard> getSaveCardLiveData;
    private PaymentStatusRepository paymentRepository;

    public PaymentStatusViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        paymentRepository = new PaymentStatusRepository();
        getSaveCardLiveData = paymentRepository.doPaymentLiveData();
    }

    public void getPaymentStatus(@NonNull String token, @NonNull String resourcePath,@NonNull String bookingId, @NonNull String cardToken, @NonNull String bookingFor) {
        paymentRepository.paymentStatus(token,resourcePath,bookingId,cardToken,bookingFor);
    }

    public LiveData<GetSaveCard> getVolumesResponseLiveData() {
        return getSaveCardLiveData;
    }
}

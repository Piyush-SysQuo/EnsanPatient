package ensan.patientapp.view.activity.paymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.paymentMethod.model.CheckOutIdResponse;
import ensan.patientapp.view.activity.paymentMethod.repository.PaymentRepository;


public class PaymentViewModel extends AndroidViewModel {

    private LiveData<CheckOutIdResponse> getSaveCardLiveData;
    private PaymentRepository paymentRepository;

    public PaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        paymentRepository = new PaymentRepository();
        getSaveCardLiveData = paymentRepository.doPaymentLiveData();
    }

    public void doPayment(@NonNull String token, @NonNull String amount, @NonNull String cardTokenId, @NonNull String bookingId) {
        paymentRepository.doPayment(token,amount,cardTokenId,bookingId);
    }

    public LiveData<CheckOutIdResponse> getVolumesResponseLiveData() {
        return getSaveCardLiveData;
    }
}

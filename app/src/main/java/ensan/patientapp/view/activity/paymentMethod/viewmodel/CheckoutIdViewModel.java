package ensan.patientapp.view.activity.paymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.paymentMethod.model.CheckOutIdResponse;
import ensan.patientapp.view.activity.paymentMethod.repository.CheckoutIdRepository;


public class CheckoutIdViewModel extends AndroidViewModel {

    private LiveData<CheckOutIdResponse> checkOutIdResponseLiveData;
    private CheckoutIdRepository checkoutIdRepository;

    public CheckoutIdViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        checkoutIdRepository = new CheckoutIdRepository();
        checkOutIdResponseLiveData = checkoutIdRepository.checkOutIdResponseLiveData();
    }

    public void getCheckoutId(@NonNull String token,String cardType) {
        checkoutIdRepository.getCheckOutId(token,cardType);
    }

    public LiveData<CheckOutIdResponse> getVolumesResponseLiveData() {
        return checkOutIdResponseLiveData;
    }
}

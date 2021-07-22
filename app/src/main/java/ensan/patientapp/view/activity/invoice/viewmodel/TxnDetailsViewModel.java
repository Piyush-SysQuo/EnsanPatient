package ensan.patientapp.view.activity.invoice.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.invoice.model.TransactionDetailsResponse;
import ensan.patientapp.view.activity.invoice.repository.TxnDetailsRepository;


public class TxnDetailsViewModel extends AndroidViewModel {

    private LiveData<TransactionDetailsResponse> transactionDetailsResponseLiveData;
    private TxnDetailsRepository txnDetailsRepository;

    public TxnDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        txnDetailsRepository = new TxnDetailsRepository();
        transactionDetailsResponseLiveData = txnDetailsRepository.transactionDetailsResponseLiveData();
    }

    public void getTxnDetails(@NonNull String token, @NonNull String bookingId) {
        txnDetailsRepository.callTxnDetails(token,bookingId);
    }

    public LiveData<TransactionDetailsResponse> getVolumesResponseLiveData() {
        return transactionDetailsResponseLiveData;
    }
}

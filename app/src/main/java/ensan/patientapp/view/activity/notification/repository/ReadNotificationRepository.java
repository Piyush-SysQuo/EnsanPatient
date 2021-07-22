package ensan.patientapp.view.activity.notification.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.notification.model.NotificationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadNotificationRepository {

    private MutableLiveData<NotificationResponse> mutableLiveData;


    public ReadNotificationRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call read notification api
    public void readNotification(@NonNull String token, @NonNull String notificationId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<NotificationResponse> call = apiDataService.readNotification("application/json","Bearer "+token,notificationId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<NotificationResponse> notificationResponseLiveData() {
        return mutableLiveData;
    }


}

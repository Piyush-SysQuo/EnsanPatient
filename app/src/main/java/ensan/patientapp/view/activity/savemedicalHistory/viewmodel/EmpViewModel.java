package ensan.patientapp.view.activity.savemedicalHistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.savemedicalHistory.model.EmploymentResponse;
import ensan.patientapp.view.activity.savemedicalHistory.repository.SaveEmpRepository;


public class EmpViewModel extends AndroidViewModel {

    private LiveData<EmploymentResponse> employmentResponseLiveData;
    private SaveEmpRepository saveEmpRepository;

    public EmpViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        saveEmpRepository = new SaveEmpRepository();
        employmentResponseLiveData = saveEmpRepository.employmentResponseLiveData();
    }

    public void saveEmp(@NonNull String token, @NonNull String working, @NonNull String detail, @NonNull String familyId) {
        saveEmpRepository.saveEmp(token,working,detail,familyId);
    }

    public LiveData<EmploymentResponse> getVolumesResponseLiveData() {
        return employmentResponseLiveData;
    }
}

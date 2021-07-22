package ensan.patientapp.view.activity.medicalhistory.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("medical")
    @Expose
    private List<Medical> medical = null;
    @SerializedName("allergy")
    @Expose
    private List<Allergy> allergy = null;
    @SerializedName("diet")
    @Expose
    private List<Diet> diet = null;
    @SerializedName("activity")
    @Expose
    private List<Activity> activity = null;
    @SerializedName("employment")
    @Expose
    private List<Employment> employment = null;

    public List<Medical> getMedical() {
        return medical;
    }

    public void setMedical(List<Medical> medical) {
        this.medical = medical;
    }

    public List<Allergy> getAllergy() {
        return allergy;
    }

    public void setAllergy(List<Allergy> allergy) {
        this.allergy = allergy;
    }

    public List<Diet> getDiet() {
        return diet;
    }

    public void setDiet(List<Diet> diet) {
        this.diet = diet;
    }

    public List<Activity> getActivity() {
        return activity;
    }

    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }

    public List<Employment> getEmployment() {
        return employment;
    }

    public void setEmployment(List<Employment> employment) {
        this.employment = employment;
    }

}
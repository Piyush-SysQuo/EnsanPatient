package ensan.patientapp.view.activity.medicalhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("smoke")
    @Expose
    private String smoke;
    @SerializedName("exercise")
    @Expose
    private String exercise;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
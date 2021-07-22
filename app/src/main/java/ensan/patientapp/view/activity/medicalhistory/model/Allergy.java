package ensan.patientapp.view.activity.medicalhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allergy {

    @SerializedName("allergy_key")
    @Expose
    private String allergyKey;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getAllergyKey() {
        return allergyKey;
    }

    public void setAllergyKey(String allergyKey) {
        this.allergyKey = allergyKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
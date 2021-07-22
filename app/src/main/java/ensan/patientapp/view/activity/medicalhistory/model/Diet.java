package ensan.patientapp.view.activity.medicalhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Diet {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private Integer id;

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
package ensan.patientapp.view.activity.medicalhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employment {

    @SerializedName("working")
    @Expose
    private String working;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
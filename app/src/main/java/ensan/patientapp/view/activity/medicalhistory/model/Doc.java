package ensan.patientapp.view.activity.medicalhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc {

    @SerializedName("doc")
    @Expose
    private String doc;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
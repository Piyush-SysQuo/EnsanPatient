package ensan.patientapp.view.activity.medicalhistory.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medical {

    @SerializedName("medical_key")
    @Expose
    private String medicalKey;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("docs")
    @Expose
    private List<Doc> docs = null;

    public String getMedicalKey() {
        return medicalKey;
    }

    public void setMedicalKey(String medicalKey) {
        this.medicalKey = medicalKey;
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

    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

}
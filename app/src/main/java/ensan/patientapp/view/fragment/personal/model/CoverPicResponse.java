
package ensan.patientapp.view.fragment.personal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoverPicResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CoverPicData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CoverPicData getData() {
        return data;
    }

    public void setData(CoverPicData data) {
        this.data = data;
    }

}

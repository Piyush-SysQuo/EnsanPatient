package ensan.patientapp.view.activity.forgotpassword.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("phone")
    @Expose
    private String phone;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


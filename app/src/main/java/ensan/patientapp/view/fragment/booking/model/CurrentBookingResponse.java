
package ensan.patientapp.view.fragment.booking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentBookingResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CurrentBookingsData currentBookingData;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CurrentBookingsData getCurrentBookingData() {
        return currentBookingData;
    }

    public void setCurrentBookingData(CurrentBookingsData currentBookingData) {
        this.currentBookingData = currentBookingData;
    }

}

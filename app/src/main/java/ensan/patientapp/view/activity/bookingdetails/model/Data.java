
package ensan.patientapp.view.activity.bookingdetails.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("care_givers")
    @Expose
    private List<CareGiver> careGivers = null;
    @SerializedName("Booking_id")
    @Expose
    private Integer bookingId;

    public List<CareGiver> getCareGivers() {
        return careGivers;
    }

    public void setCareGivers(List<CareGiver> careGivers) {
        this.careGivers = careGivers;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

}

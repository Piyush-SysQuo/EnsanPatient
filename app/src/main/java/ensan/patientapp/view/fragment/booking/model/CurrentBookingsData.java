
package ensan.patientapp.view.fragment.booking.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentBookingsData {

    @SerializedName("bookings")
    @Expose
    private List<CurrentBooking> bookings = null;

    public List<CurrentBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<CurrentBooking> bookings) {
        this.bookings = bookings;
    }

}

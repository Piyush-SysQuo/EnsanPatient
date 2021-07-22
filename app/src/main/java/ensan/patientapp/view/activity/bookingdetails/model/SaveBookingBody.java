package ensan.patientapp.view.activity.bookingdetails.model;

public class SaveBookingBody {

    private String booking_id;
    private String care_giver_id;
    private String notes;

    public SaveBookingBody(String booking_id, String care_giver_id, String notes) {
        this.booking_id = booking_id;
        this.care_giver_id = care_giver_id;
        this.notes = notes;
    }

    public String getBookingId() {
        return booking_id;
    }

    public void setBookingId(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getCare_giver_id() {
        return care_giver_id;
    }

    public void setCare_giver_id(String care_giver_id) {
        this.care_giver_id = care_giver_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

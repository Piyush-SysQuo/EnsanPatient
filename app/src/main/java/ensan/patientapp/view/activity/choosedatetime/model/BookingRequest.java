package ensan.patientapp.view.activity.choosedatetime.model;

public class BookingRequest {

    private String booked_for;
    private String cat_id;
    private String sub_cat_id;
    private String from_date;
    private String to_date;
    private String time;
    private String frequeny_id;
    private String language;
    private String family_member_id;

    public BookingRequest(String booked_for, String cat_id, String sub_cat_id, String from_date, String to_date, String time, String frequeny_id, String language, String family_member_id) {
        this.booked_for = booked_for;
        this.cat_id = cat_id;
        this.sub_cat_id = sub_cat_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.time = time;
        this.frequeny_id = frequeny_id;
        this.language = language;
        this.family_member_id = family_member_id;
    }

    public String getBooked_for() {
        return booked_for;
    }

    public void setBooked_for(String booked_for) {
        this.booked_for = booked_for;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrequeny_id() {
        return frequeny_id;
    }

    public void setFrequeny_id(String frequeny_id) {
        this.frequeny_id = frequeny_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}


package ensan.patientapp.view.fragment.booking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentBooking {

    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("cat")
    @Expose
    private String cat;
    @SerializedName("sub_cat")
    @Expose
    private String subCat;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("care_giver_id")
    @Expose
    private String careGiverId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_review")
    @Expose
    private boolean is_review;
    @SerializedName("patient_lat")
    @Expose
    private String patient_lat;
    @SerializedName("patient_long")
    @Expose
    private String patient_long;
    @SerializedName("caregiver_lat")
    @Expose
    private String caregiver_lat;
    @SerializedName("caregiver_long")
    @Expose
    private String caregiver_long ;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCareGiverId() {
        return careGiverId;
    }

    public void setCareGiverId(String careGiverId) {
        this.careGiverId = careGiverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_review() {
        return is_review;
    }

    public void setIs_review(boolean is_review) {
        this.is_review = is_review;
    }


    public String getPatient_lat() {
        return patient_lat;
    }

    public void setPatient_lat(String patient_lat) {
        this.patient_lat = patient_lat;
    }

    public String getPatient_long() {
        return patient_long;
    }

    public void setPatient_long(String patient_long) {
        this.patient_long = patient_long;
    }

    public String getCaregiver_lat() {
        return caregiver_lat;
    }

    public void setCaregiver_lat(String caregiver_lat) {
        this.caregiver_lat = caregiver_lat;
    }

    public String getCaregiver_long() {
        return caregiver_long;
    }

    public void setCaregiver_long(String caregiver_long) {
        this.caregiver_long = caregiver_long;
    }
}

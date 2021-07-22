
package ensan.patientapp.view.activity.bookingdetails.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingDetailsData{

    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("care_giver_id")
    @Expose
    private Integer careGiverId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("cat")
    @Expose
    private String cat;
    @SerializedName("sub_cat")
    @Expose
    private List<SubCat> subCat = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("addiitional_charges")
    @Expose
    private List<AddiitionalCharge> addiitionalCharges = null;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("extraPayment")
    @Expose
    private Integer extraPayment;
    @SerializedName("patient_lat")
    @Expose
    private String patient_lat;
    @SerializedName("patient_long")
    @Expose
    private String patient_long;
    @SerializedName("caregiver_lat")
    @Expose
    private String caregiver_lat;
    @SerializedName("caregiver_long ")
    @Expose
    private String caregiver_long ;

    public List<SubCat> getSubCat() {
        return subCat;
    }

    public void setSubCat(List<SubCat> subCat) {
        this.subCat = subCat;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getCareGiverId() {
        return careGiverId;
    }

    public void setCareGiverId(Integer careGiverId) {
        this.careGiverId = careGiverId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AddiitionalCharge> getAddiitionalCharges() {
        return addiitionalCharges;
    }

    public void setAddiitionalCharges(List<AddiitionalCharge> addiitionalCharges) {
        this.addiitionalCharges = addiitionalCharges;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getExtraPayment() {
        return extraPayment;
    }

    public void setExtraPayment(Integer extraPayment) {
        this.extraPayment = extraPayment;
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

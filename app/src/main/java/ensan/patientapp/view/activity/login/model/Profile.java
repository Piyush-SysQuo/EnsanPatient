package ensan.patientapp.view.activity.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("basic_info")
    @Expose
    private Integer basicInfo;
    @SerializedName("profile_info")
    @Expose
    private Integer profileInfo;
    @SerializedName("address")
    @Expose
    private Integer address;
    @SerializedName("document")
    @Expose
    private Integer document;
    @SerializedName("profile_complete")
    @Expose
    private Integer profileComplete;

    public Integer getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(Integer basicInfo) {
        this.basicInfo = basicInfo;
    }

    public Integer getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(Integer profileInfo) {
        this.profileInfo = profileInfo;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public Integer getDocument() {
        return document;
    }

    public void setDocument(Integer document) {
        this.document = document;
    }

    public Integer getProfileComplete() {
        return profileComplete;
    }

    public void setProfileComplete(Integer profileComplete) {
        this.profileComplete = profileComplete;
    }
}


package ensan.patientapp.view.fragment.personal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import ensan.patientapp.view.fragment.personal.model.User;

@Parcel
public class Data {

    @SerializedName("User")
    @Expose
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}


package ensan.patientapp.view.activity.bookingdetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddiitionalCharge {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}

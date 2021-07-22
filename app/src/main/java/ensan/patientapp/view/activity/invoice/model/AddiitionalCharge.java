
package ensan.patientapp.view.activity.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddiitionalCharge {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}

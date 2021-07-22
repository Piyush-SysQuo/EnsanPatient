
package ensan.patientapp.view.activity.bookingdetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCat {

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("subcat")
    @Expose
    private String subcat;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

}

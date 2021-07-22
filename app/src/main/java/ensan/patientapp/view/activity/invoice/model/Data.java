
package ensan.patientapp.view.activity.invoice.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("services")
    @Expose
    private List<Service> services = null;
    @SerializedName("addiitional_charges")
    @Expose
    private List<AddiitionalCharge> addiitionalCharges = null;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("caregiver_address")
    @Expose
    private String caregiverAddress;
    @SerializedName("caregiver_name")
    @Expose
    private String caregiverName;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("invoice_file")
    @Expose
    private String invoiceFile;
    @SerializedName("due_amount")
    @Expose
    private String due_amount ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<AddiitionalCharge> getAddiitionalCharges() {
        return addiitionalCharges;
    }

    public void setAddiitionalCharges(List<AddiitionalCharge> addiitionalCharges) {
        this.addiitionalCharges = addiitionalCharges;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCaregiverAddress() {
        return caregiverAddress;
    }

    public void setCaregiverAddress(String caregiverAddress) {
        this.caregiverAddress = caregiverAddress;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }
}

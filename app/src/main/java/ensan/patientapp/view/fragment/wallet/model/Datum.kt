package ensan.patientapp.view.fragment.wallet.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("invoice_no")
    @Expose
    var invoiceNo: String? = null

    @SerializedName("booking_id")
    @Expose
    var bookingId: String? = null
}
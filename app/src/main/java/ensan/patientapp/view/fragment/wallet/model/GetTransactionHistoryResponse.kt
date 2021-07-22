package ensan.patientapp.view.fragment.wallet.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetTransactionHistoryResponse(@SerializedName("success")
                                    @Expose var success: Boolean?, @SerializedName("message")
                                    @Expose var message: String?, @SerializedName("data")
                                    @Expose var data: List<Datum>?, @SerializedName("total_spent")
                                    @Expose var totalSpent: String?,  @SerializedName ("booking_id")
                                    @Expose var bookingId: String?)

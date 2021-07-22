package ensan.patientapp.view.activity.review.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RatingResponse {

    @SerializedName("success")
    @Expose
    private var success: Boolean? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null


    fun getSuccess(): Boolean? {
        return success
    }

    fun getMessage(): String? {
        return message
    }

}
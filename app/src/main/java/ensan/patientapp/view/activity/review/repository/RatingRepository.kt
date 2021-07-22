package ensan.patientapp.view.activity.review.repository

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ensan.patientapp.connection.ApiDataService
import ensan.patientapp.connection.RetrofitInstance
import ensan.patientapp.view.activity.review.model.RatingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RatingRepository {

    private var mutableLiveData: MutableLiveData<RatingResponse>? = null

    constructor(){
        mutableLiveData = MutableLiveData()
    }

    fun callRatingApi(@NonNull token: String, @NonNull caregiverId: String,
                             @NonNull message: String, @NonNull rating: String, @NonNull bookingId: String){
        val apiDataService = RetrofitInstance.getInstance().create(ApiDataService::class.java)
        val  call: Call<RatingResponse> = apiDataService.rating("application/json", "Bearer $token", caregiverId, message, rating,bookingId)
        call.enqueue(object : Callback<RatingResponse?> {
            override fun onResponse(call: Call<RatingResponse?>, response: Response<RatingResponse?>) {
                if (response.body() != null) {
                    mutableLiveData!!.postValue(response.body())
                } else {
                    mutableLiveData!!.postValue(null)
                }
            }

            override fun onFailure(call: Call<RatingResponse?>, t: Throwable) {
                mutableLiveData!!.postValue(null)
            }
        })
    }

    fun ratingRespLiveData(): LiveData<RatingResponse>? {
        return mutableLiveData
    }

}
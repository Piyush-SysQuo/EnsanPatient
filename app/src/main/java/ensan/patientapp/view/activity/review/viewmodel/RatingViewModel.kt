package ensan.patientapp.view.activity.review.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ensan.patientapp.view.activity.review.repository.RatingRepository
import ensan.patientapp.view.activity.review.model.RatingResponse


class RatingViewModel(application: Application) : AndroidViewModel(application) {

    var ratingRespLiveData: LiveData<RatingResponse>? = null
    private var ratingRepository: RatingRepository? = null

     fun init(){
        ratingRepository = RatingRepository()
        ratingRespLiveData = ratingRepository!!.ratingRespLiveData()
    }

    fun rating(@NonNull token: String, @NonNull caregiverId: String,
                     @NonNull message: String, @NonNull rating: String,  @NonNull bookingId: String){
       ratingRepository?.callRatingApi(token, caregiverId, message, rating,bookingId)
   }

    fun getVolumesResponseLiveData(): LiveData<RatingResponse>? {
        return ratingRespLiveData
    }
}
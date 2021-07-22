package ensan.patientapp.view.activity.paymentMethod.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ensan.patientapp.R
import ensan.patientapp.view.activity.paymentMethod.model.CardSelectionType
import kotlinx.android.synthetic.main.fragment_card_select.view.*

class CardSelectFragment constructor(var cardSelectionType: CardSelectionType) : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var statusCode : String? = null

        val  view = inflater.inflate(R.layout.fragment_card_select, container, false)

        // default for visa
        statusCode = "1"

        view.continueClickBtn.setOnClickListener {
           this.cardSelectionType.cardType(statusCode.toString())
           dismiss()
        }

        // cross button clicked
        view.img_cancel.setOnClickListener{
            dismiss()
        }


        // status code 1 - Visa / Meastro
        //  0 - Mada
        view.visa.setOnClickListener{
            statusCode = "1"
        }

        view.mada.setOnClickListener {
            statusCode = "0"
        }

        return view
    }
}
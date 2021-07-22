package ensan.caregiverapp.view.activity.invoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import ensan.patientapp.R
import ensan.patientapp.view.activity.invoice.model.AddiitionalCharge
import kotlinx.android.synthetic.main.invoice_layout.view.*

class ExtraChargeAdapter(@Expose var context: Context?, @Expose var data: List<AddiitionalCharge>?) : RecyclerView.Adapter<ExtraChargeAdapter.MyViewHolder>() {


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.invoice_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

                 holder.itemView.apply {
                     headingTV.text = data?.get(position)?.name
                     earningTV.text = data?.get(position)?.price
                     subHeadingTV.visibility = View.GONE
                 }
                 
    }

    override fun getItemCount(): Int = data?.size!!
}
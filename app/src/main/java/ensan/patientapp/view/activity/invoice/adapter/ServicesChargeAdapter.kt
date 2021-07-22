package ensan.caregiverapp.view.activity.invoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import ensan.patientapp.R
import ensan.patientapp.view.activity.invoice.model.Service
import kotlinx.android.synthetic.main.invoice_layout.view.*

class ServicesChargeAdapter(@Expose var context: Context?, @Expose var data: List<Service>?) : RecyclerView.Adapter<ServicesChargeAdapter.MyViewHolder>() {


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.invoice_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                 holder.itemView.headingTV.text = data?.get(position)?.name
                 holder.itemView.earningTV.text = data?.get(position)?.price
                 holder.itemView.subHeadingTV.text = data?.get(position)?.frequency
    }

    override fun getItemCount(): Int = data?.size!!
}
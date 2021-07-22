package ensan.patientapp.view.fragment.wallet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import ensan.patientapp.R
import ensan.patientapp.view.fragment.wallet.model.Datum
import ensan.patientapp.view.fragment.wallet.model.GetTransactionPosition
import kotlinx.android.synthetic.main.transaction_list.view.*

class WalletAdapter(@Expose var context: Context?,  @Expose var data: List<Datum>?, @Expose var txnPosition: GetTransactionPosition) : RecyclerView.Adapter<WalletAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.txt_name.text = data?.get(position)?.name
            holder.itemView.txt_invoice.text = context?.getString(R.string.invoice_no)+data?.get(position)?.invoiceNo
            holder.itemView.txt_date.text = data?.get(position)?.date
            holder.itemView.txt_amt.text = data?.get(position)?.amount

            // set txn position
           holder.itemView.mainLayout.setOnClickListener{
               txnPosition.position(position)
           }
    }

    override fun getItemCount(): Int = data?.size!!
}
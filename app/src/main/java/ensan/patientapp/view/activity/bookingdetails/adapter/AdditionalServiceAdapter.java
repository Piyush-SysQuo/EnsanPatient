package ensan.patientapp.view.activity.bookingdetails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.bookingdetails.model.AddiitionalCharge;

public class AdditionalServiceAdapter extends RecyclerView.Adapter<AdditionalServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<AddiitionalCharge> chargeList;

    public AdditionalServiceAdapter(Context mContext, List<AddiitionalCharge> chargeList) {
        this.mContext = mContext;
        this.chargeList = chargeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extra_service_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.txt_service.setText(chargeList.get(position).getKey());
       holder.txt_price.setText(chargeList.get(position).getAmount());


    }

    @Override
    public int getItemCount() {
        return chargeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_service, txt_price, price_total;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_service =  itemView.findViewById(R.id.txt_service);
            txt_price =  itemView.findViewById(R.id.txt_price);

        }
    }
}

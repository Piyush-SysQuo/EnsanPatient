package ensan.patientapp.view.activity.paymentMethod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.paymentMethod.model.Datum;
import ensan.patientapp.view.activity.paymentMethod.model.GetCardPosition;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> datumList;
    private GetCardPosition getCardPosition;
    private int row_index;

    public PaymentMethodAdapter(Context context, List<Datum> datumList, GetCardPosition getCardPosition) {
        this.context = context;
        this.datumList = datumList;
        this.getCardPosition = getCardPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_card_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.cardNo.setText("XXXX XXXX XXXX "+datumList.get(position).getCardNumber());

            // delete card
            holder.delete.setOnClickListener(v -> {
                    getCardPosition.cardPosition(position);
            });

            holder.name.setText(datumList.get(position).getCardHolderName());

            holder.expDate.setText(datumList.get(position).getExpiryMonth() +"/"+datumList.get(position).getExpiryYear());


            holder.layout_info.setOnClickListener(v -> {
                holder.imgSelect.setVisibility(View.VISIBLE);
                getCardPosition.getCardId(datumList.get(position).getCardToken());
                row_index = position;
                notifyDataSetChanged();
            });

        if (row_index == position) {
            holder.imgSelect.setVisibility(View.VISIBLE);
            if(position == 0){
                getCardPosition.getCardId(datumList.get(position).getCardToken());
            }
        } else {
            holder.imgSelect.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView cardNo, name,expDate;
        private ImageView delete, imgSelect;
        private LinearLayout layout_info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNo =  itemView.findViewById(R.id.cardNo);
            name =  itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
            expDate =  itemView.findViewById(R.id.expDate);
            layout_info = itemView.findViewById(R.id.layout_info);
            imgSelect = itemView.findViewById(R.id.imgSelect);
        }
    }

    public void deleteCard(Datum datum){

        if(datumList == null){

            datumList = new ArrayList<>();

        }

        datumList.remove(datum);

        notifyDataSetChanged();
    }
}

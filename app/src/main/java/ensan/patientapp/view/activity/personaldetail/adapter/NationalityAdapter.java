package ensan.patientapp.view.activity.personaldetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.personaldetail.model.GetNationality;


public class NationalityAdapter extends RecyclerView.Adapter<NationalityAdapter.MyViewHolder> {

    private String[] nationalityList;
    private Context mContext;
    private GetNationality getNationality;

    public NationalityAdapter(String[] nationalityList, Context mContext, GetNationality getNationality) {
        this.nationalityList = Arrays.copyOfRange(nationalityList, 1, nationalityList.length);
        this.mContext = mContext;
        this.getNationality = getNationality;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marital_status_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.statusTV.setText(nationalityList[position]);

        holder.statusLayout.setOnClickListener(v -> {
            holder.statusLayout.setBackground(mContext.getDrawable(R.drawable.blue_strok_rect));
            getNationality.getNationality(position);
        });
    }

    @Override
    public int getItemCount() {
        return nationalityList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout statusLayout;
        private TextView statusTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTV = itemView.findViewById(R.id.statusTV);
            statusLayout =  itemView.findViewById(R.id.statusLayout);
        }
    }


}


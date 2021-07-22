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
import ensan.patientapp.view.activity.personaldetail.model.GetMaritalStatus;


public class MaritalStatusAdapter extends RecyclerView.Adapter<MaritalStatusAdapter.MyViewHolder> {

    private String[] marritalStatusList;
    private Context mContext;
    private GetMaritalStatus getMaritalStatus;

    public MaritalStatusAdapter(String[] marritalStatusList, Context mContext, GetMaritalStatus getMaritalStatus) {
//        this.marritalStatusList = marritalStatusList;
        this.marritalStatusList = Arrays.copyOfRange(marritalStatusList, 1, marritalStatusList.length);
        this.mContext = mContext;
        this.getMaritalStatus = getMaritalStatus;
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
        holder.statusTV.setText(marritalStatusList[position]);

        holder.statusLayout.setOnClickListener(v -> {
            holder.statusLayout.setBackground(mContext.getDrawable(R.drawable.blue_strok_rect));
            getMaritalStatus.setMaritalStatus(position);
        });
    }

    @Override
    public int getItemCount() {
        return marritalStatusList.length;
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

    // add all status to the list
    public void addAllMaritalStatus(String[] marritalStatusList){

        if(marritalStatusList == null){

            marritalStatusList = new String[]{};

        }

        marritalStatusList = marritalStatusList;

        notifyDataSetChanged();
    }


}


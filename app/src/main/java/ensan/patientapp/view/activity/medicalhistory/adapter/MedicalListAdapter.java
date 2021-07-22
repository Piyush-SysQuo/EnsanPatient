package ensan.patientapp.view.activity.medicalhistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.medicalhistory.model.Medical;

public class MedicalListAdapter extends RecyclerView.Adapter<MedicalListAdapter.MyViewHolder> {

    private List<Medical> medicalList;
    private Context mContext;

    public MedicalListAdapter(List<Medical> medicalList, Context mContext) {
        this.medicalList = medicalList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medical_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Glide.with(mContext).load(medicalList.get(position).getDocs().get(0).getDoc()).into(holder.imageView);
       holder.valueTV.setText(medicalList.get(position).getMedicalKey());
       holder.idTV.setText(medicalList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return medicalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView idTV, valueTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView);
            idTV =  itemView.findViewById(R.id.idTV);
            valueTV =  itemView.findViewById(R.id.valueTV);
        }
    }
}

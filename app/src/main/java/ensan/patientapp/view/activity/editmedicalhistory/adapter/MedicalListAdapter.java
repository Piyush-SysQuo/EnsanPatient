package ensan.patientapp.view.activity.editmedicalhistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

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
                .inflate(R.layout.medical_history_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (medicalList==null || medicalList.get(position).getDocs().size()>0){
            Glide.with(mContext).load(medicalList.get(position).getDocs().get(0).getDoc()).into(holder.imageView);
        }
       holder.detailET.setText(medicalList.get(position).getMedicalKey());
    }

    @Override
    public int getItemCount() {
        return medicalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextInputEditText detailET;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView);
            detailET =  itemView.findViewById(R.id.detailET);
        }
    }
}

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

public class MedicalListAdapterNew extends RecyclerView.Adapter<MedicalListAdapterNew.MyViewHolder> {

    private List<Medical> medicalList;
    private Context mContext;

    public MedicalListAdapterNew(List<Medical> medicalList, Context mContext) {
        this.medicalList = medicalList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medical_list_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (medicalList==null || medicalList.get(position).getDocs().size()>0){
            Glide.with(mContext).load(medicalList.get(position).getDocs().get(0).getDoc()).into(holder.imageView);
        }
       holder.descTV.setText(medicalList.get(position).getMedicalKey());
        String title = "";
        if(medicalList.get(position).getValue().contains("Surgical")){
            title = medicalList.get(position).getValue();
        }else{
            title = medicalList.get(position).getValue() +" "+ mContext.getString(R.string.history);
        }
        holder.titleTV.setText(title);
    }

    @Override
    public int getItemCount() {
        return medicalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleTV, descTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView);
            titleTV =  itemView.findViewById(R.id.titleTV);
            descTV =  itemView.findViewById(R.id.descriptionTV);
        }
    }
}

package ensan.patientapp.view.activity.idinsurance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;

import java.util.List;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.idinsurance.model.Data;
import ensan.patientapp.view.activity.idinsurance.model.GetInsDocPosition;

public class InsuranceDocAdapter extends RecyclerView.Adapter<InsuranceDocAdapter.MyViewHolder> {

    private Context context;
    private List<Data> dataList;
    private GetInsDocPosition getInsDocPosition;

    public InsuranceDocAdapter(Context context, List<Data> dataList, GetInsDocPosition getInsDocPosition) {
        this.context = context;
        this.dataList = dataList;
        this.getInsDocPosition = getInsDocPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.docslist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDocument()).into(holder.proofImg);

        //delete an doc
        holder.proofDeleteImg.setOnClickListener(v -> getInsDocPosition.getPosition(position));

        if(dataList.size() == 1){
            holder.proofDeleteImg.setVisibility(View.GONE);
        }else {
            holder.proofDeleteImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView proofImg;
        private ImageView proofDeleteImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            proofImg = itemView.findViewById(R.id.proofImg);
            proofDeleteImg = itemView.findViewById(R.id.proofDeleteImg);

        }
    }

    public void deleteImage(Data image){

        dataList.remove(image);

        notifyDataSetChanged();
    }
}

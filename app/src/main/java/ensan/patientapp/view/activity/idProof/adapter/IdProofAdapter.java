package ensan.patientapp.view.activity.idProof.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.idProof.model.Data;
import ensan.patientapp.view.activity.idProof.model.GetIdImagePosition;

public class IdProofAdapter extends RecyclerView.Adapter<IdProofAdapter.MyViewHolder> {

    private List<Data> stringList;
    private Context context;
    private GetIdImagePosition getIdImagePosition;

    public IdProofAdapter(List<Data> stringList, Context context, GetIdImagePosition getIdImagePosition) {
        this.stringList = stringList;
        this.context = context;
        this.getIdImagePosition = getIdImagePosition;
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

        Glide.with(context).load(stringList.get(position).getDocument()).into(holder.proofImg);

        holder.proofDeleteImg.setOnClickListener(v -> getIdImagePosition.position(position));

        if(stringList.size() == 1){
            holder.proofDeleteImg.setVisibility(View.GONE);
        }else {
            holder.proofDeleteImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView proofImg, proofDeleteImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            proofImg = itemView.findViewById(R.id.proofImg);
            proofDeleteImg = itemView.findViewById(R.id.proofDeleteImg);
        }
    }

    public void deleteImage(Data data) {
        stringList.remove(data);
        notifyDataSetChanged();
    }

}

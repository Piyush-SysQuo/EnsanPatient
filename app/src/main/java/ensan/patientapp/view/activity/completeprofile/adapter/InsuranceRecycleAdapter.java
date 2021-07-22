package ensan.patientapp.view.activity.completeprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.completeprofile.model.InsuranceImagePosition;


public class InsuranceRecycleAdapter extends RecyclerView.Adapter<InsuranceRecycleAdapter.MyViewHolder> {

    private ArrayList<Image> images;
    private Context mContext;
    private InsuranceImagePosition imageVerificationPosition;

    public InsuranceRecycleAdapter(ArrayList<Image> images, Context mContext, InsuranceImagePosition imageVerificationPosition) {
        this.images = images;
        this.mContext = mContext;
        this.imageVerificationPosition = imageVerificationPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load(images.get(position).getUri())
                .into(holder.selectImg);

        holder.img_delete.setOnClickListener(v -> {
            imageVerificationPosition.imageInsurancePosition(position);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView selectImg;
        private ImageView img_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            selectImg =  itemView.findViewById(R.id.selectImg);
            img_delete =  itemView.findViewById(R.id.img_delete);
        }
    }


    // remove image from list
    public void deleteImage(Image image){

        if(images != null) {
            images.remove(image);
        }

        notifyDataSetChanged();
    }

    // add image to list
    public void addImage(Image image){

        if(images == null){
            images = new ArrayList<>();
        }

        images.add(image);

        notifyDataSetChanged();
    }

    // add all images  to the list
    public void allAllImage(ArrayList<Image> arrayList){

        if(images == null){

            images = new ArrayList<>();

        }

        images = arrayList;

        notifyDataSetChanged();
    }
}

package ensan.patientapp.view.activity.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.home.model.Datum;
import ensan.patientapp.view.activity.home.model.GetCategoryPosition;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {


    private Context mContext;
    private List<Datum> list;
    private GetCategoryPosition categoryPosition;

    public ServiceListAdapter(Context mContext, List<Datum> list, GetCategoryPosition categoryPosition) {
        this.mContext = mContext;
        this.list = list;
        this.categoryPosition = categoryPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.srvce_list_home_adapter_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.descTv.setText(list.get(position).getEnName());
        Glide.with(mContext).load(list.get(position).getImage()).into(holder.imageView);
        holder.mainLayout.setOnClickListener(v -> categoryPosition.getPosition(position,false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        ImageView imageView;
        TextView descTv;

        ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageView = itemView.findViewById(R.id.imageView);
            descTv = itemView.findViewById(R.id.descTv);
        }
    }

    // add all data to adapter
    public void setData(List<Datum> datumList){

        if (list == null){
            list = new ArrayList<>();
        }

        list = datumList;
        notifyDataSetChanged();
    }

    public void filterItem(List<Datum> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }
}

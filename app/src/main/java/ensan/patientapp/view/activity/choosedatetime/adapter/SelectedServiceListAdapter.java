package ensan.patientapp.view.activity.choosedatetime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.home.model.Datum;

public class SelectedServiceListAdapter extends RecyclerView.Adapter<SelectedServiceListAdapter.ViewHolder> {

    private Context mContext;
    private List<Datum> list;
    private boolean isChecked = false;

    public SelectedServiceListAdapter(Context mContext, List<Datum> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.srvce_list_home_adapter_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.descTv.setText(list.get(position).getEnName());
        Glide.with(mContext).load(list.get(position).getImage()).into(holder.antibioticIv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mainLayout;
        private TextView descTv;
        private ImageView antibioticIv;
        LinearLayout bgLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            antibioticIv = itemView.findViewById(R.id.imageView);
            descTv = itemView.findViewById(R.id.descTv);
            bgLayout = itemView.findViewById(R.id.bgLayout);
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
}

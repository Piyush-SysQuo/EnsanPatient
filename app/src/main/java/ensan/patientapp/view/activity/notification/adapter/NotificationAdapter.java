package ensan.patientapp.view.activity.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.notification.model.Datum;
import ensan.patientapp.view.activity.notification.model.NotificationPosition;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<Datum> datumArrayList;
    private NotificationPosition notificationPosition;

    public NotificationAdapter(Context mContext, List<Datum> datumArrayList,NotificationPosition notificationPosition) {
        this.mContext = mContext;
        this.datumArrayList = datumArrayList;
        this.notificationPosition = notificationPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
              holder.dateTv.setText(datumArrayList.get(position).getDate());
              holder.msgTv.setText(datumArrayList.get(position).getMessage());

              holder.layoutNoti.setOnClickListener(v -> {
                     notificationPosition.notificationPosition(position);
              });
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView dateTv, msgTv;
        private RelativeLayout layoutNoti;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.dateTv);
            msgTv = itemView.findViewById(R.id.msgTv);
            layoutNoti = itemView.findViewById(R.id.layoutNoti);
        }
    }


    public void removeNotification(Datum datum){

        if(datumArrayList == null){
            datumArrayList = new ArrayList<>();
        }

        datumArrayList.remove(datum);

        notifyDataSetChanged();
    }
}

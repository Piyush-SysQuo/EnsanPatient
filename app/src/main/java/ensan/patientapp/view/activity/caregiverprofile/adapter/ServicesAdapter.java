package ensan.patientapp.view.activity.caregiverprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ensan.patientapp.R;

public class ServicesAdapter  extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private Context mContext;
    private List<String> serviceList;

    public ServicesAdapter(Context mContext, List<String> serviceList) {
        this.mContext = mContext;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_layout, parent, false);
        return new ServiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
           holder.serviceTV.setText(serviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceTV;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceTV =   itemView.findViewById(R.id.serviceTV);
        }
    }
}

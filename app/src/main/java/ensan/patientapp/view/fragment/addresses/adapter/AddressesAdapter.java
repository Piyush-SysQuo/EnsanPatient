package ensan.patientapp.view.fragment.addresses.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.fragment.addresses.model.Datum;
import ensan.patientapp.view.fragment.addresses.model.DefaultAddress;
import ensan.patientapp.view.fragment.addresses.model.DeleteAddress;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private Context mContext;
    List<Datum> addressList;
    DeleteAddress deleteAddress;
    DefaultAddress defaultAddress;

    public AddressesAdapter(Context mContext, List<Datum> addressList, DeleteAddress addressesFragment, DefaultAddress defaultAddress) {
        this.mContext = mContext;
        this.addressList = addressList;
        this.deleteAddress = addressesFragment;
        this.defaultAddress = defaultAddress;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addresses_item, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        // holder.mainLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), BookingDetailsActivity.class)));
        if (addressList.size()<=1){
            holder.deleteAddress.setVisibility(View.GONE);
        }

        holder.txt_address.setText(addressList.get(i).getAddress());

        holder.deleteAddress.setOnClickListener(view -> {
            if (addressList.get(i).getId() != null) {
                deleteAddress.getItemPosition(addressList.get(i).getId());
            }
            if (isNetworkAvailable(mContext)) {
                addressList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, addressList.size());
            }
            notifyDataSetChanged();
        });

        holder.tag.setText(addressList.get(i).getTag());

        holder.addAddresscard.setOnClickListener(v -> {
            defaultAddress.getAddressPosition(i);
        });



    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_address, tag;
        private ImageView deleteAddress;
        private CardView addAddresscard;

        ViewHolder(View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.address);
            deleteAddress = itemView.findViewById(R.id.delete);
            addAddresscard = itemView.findViewById(R.id.addAddresscard);
            tag = itemView.findViewById(R.id.tag);
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
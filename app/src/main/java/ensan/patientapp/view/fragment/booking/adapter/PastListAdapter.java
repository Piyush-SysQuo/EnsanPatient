package ensan.patientapp.view.fragment.booking.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ensan.patientapp.R;
import ensan.patientapp.view.fragment.booking.model.CurrentBooking;
import ensan.patientapp.view.fragment.booking.model.GetCurrentPosition;
import ensan.patientapp.view.fragment.booking.model.GetPosition;
import ensan.patientapp.view.fragment.booking.model.ViewProfilePosition;

public class PastListAdapter extends RecyclerView.Adapter<PastListAdapter.ViewHolder> {

    private Context context;
    private List<CurrentBooking> bookingList;
    private GetCurrentPosition getCurrentPosition;
    private ViewProfilePosition viewProfilePosition;
    private GetPosition getPosition;

    public PastListAdapter(Context context, List<CurrentBooking> bookingList, GetCurrentPosition getCurrentPosition, ViewProfilePosition viewProfilePosition, GetPosition getPosition) {
        this.context = context;
        this.bookingList = bookingList;
        this.getCurrentPosition = getCurrentPosition;
        this.viewProfilePosition = viewProfilePosition;
        this.getPosition = getPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_adapter_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Glide.with(context).load(bookingList.get(position).getProfilePic()).into(holder.userImg);
        holder.txt_username.setText(bookingList.get(position).getName());
        holder.txt_cat.setText(bookingList.get(position).getCat());
        holder.txt_price.setText(bookingList.get(position).getPrice());
        holder.txt_date.setText(bookingList.get(position).getDate());

        holder.txt_profile.setOnClickListener(v -> {
            viewProfilePosition.getProfilePosition(position);
        });

        if(bookingList.get(position).isIs_review()){
            holder.txt_profile.setVisibility(View.INVISIBLE);
        }

     holder.img_call.setOnClickListener(v -> {
            enableRunTimePermission(bookingList.get(position).getPhone());
        });

     holder.mainLayout.setOnClickListener(v -> {
         getPosition.getPatientPosition(position,"past");
     });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mainLayout;
        private LinearLayout cancelLayout;
        private CircleImageView userImg;
        private TextView txt_username, txt_profile;
        private TextView txt_cat, txt_price, txt_date;
        private ImageView img_call;

        ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);
            userImg = itemView.findViewById(R.id.userImg);
            img_call = itemView.findViewById(R.id.img_call);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_cat = itemView.findViewById(R.id.txt_cat);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_profile = itemView.findViewById(R.id.txt_profile);

        }
    }

    // enable run time permission
    private void enableRunTimePermission(String phone){
        Dexter.withContext(context).withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phone));//change the number
                        context.startActivity(callIntent);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}
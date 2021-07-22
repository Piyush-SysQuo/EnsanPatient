package ensan.patientapp.view.activity.bookingdetails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.bookingdetails.model.CareGiver;
import ensan.patientapp.view.activity.bookingdetails.model.GetListPosition;
import ensan.patientapp.view.activity.bookingdetails.model.ViewProfilePosition;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyBookingViewHolder> {

    private List<CareGiver> careGiverList;
    private Context mContext;
    private GetListPosition listPosition;
    private ViewProfilePosition viewProfilePosition;

    public BookingAdapter(List<CareGiver> careGiverList, Context mContext, GetListPosition listPosition, ViewProfilePosition viewProfilePosition) {
        this.careGiverList = careGiverList;
        this.mContext = mContext;
        this.listPosition = listPosition;
        this.viewProfilePosition = viewProfilePosition;
    }

    @NonNull
    @Override
    public MyBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list, parent, false);
        return new MyBookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingViewHolder holder, int position) {
         holder.txt_name.setText(careGiverList.get(position).getName());
         Glide.with(mContext).load(careGiverList.get(position).getProfilePic()).into(holder.userImg);
         holder.txt_country.setText(careGiverList.get(position).getCountry());
         holder.btn_book.setOnClickListener(v -> {
             String notes = holder.et_notes.getText().toString().trim();
             listPosition.getCareGiverPosition(position,notes);
         });
         holder.ratingBar.setRating(Float.parseFloat(careGiverList.get(position).getRating()));
         holder.txt_date_time.setText(careGiverList.get(position).getDate()+"  "+careGiverList.get(position).getTime());
         holder.txt_subCat.setText(careGiverList.get(position).getCat());
         holder.txt_price.setText(careGiverList.get(position).getPrice() +" SAR");

         holder.txt_profile.setOnClickListener(v -> {
           viewProfilePosition.getProfilePosition(position);
         });
    }

    @Override
    public int getItemCount() {
        return careGiverList.size();
    }

    public class MyBookingViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImg;
        private TextView txt_name, txt_country,btn_book,txt_date_time, txt_subCat, txt_price;
        private EditText et_notes;
        private RatingBar ratingBar;
        private TextView txt_profile;

        public MyBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.userImg);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_country = itemView.findViewById(R.id.txt_country);
            btn_book = itemView.findViewById(R.id.btn_book);
            et_notes = itemView.findViewById(R.id.et_notes);
            ratingBar = itemView.findViewById(R.id.rating);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_subCat = itemView.findViewById(R.id.txt_subCat);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_profile = itemView.findViewById(R.id.txt_profile);
        }
    }
}

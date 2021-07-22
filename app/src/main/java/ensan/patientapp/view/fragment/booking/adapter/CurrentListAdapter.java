package ensan.patientapp.view.fragment.booking.adapter;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ensan.patientapp.R;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.fragment.booking.model.CurrentBooking;
import ensan.patientapp.view.fragment.booking.model.GetCallPosition;
import ensan.patientapp.view.fragment.booking.model.GetCurrentPosition;
import ensan.patientapp.view.fragment.booking.model.GetPosition;
import ensan.patientapp.view.fragment.booking.model.ViewProfilePosition;
import ensan.patientapp.view.fragment.support.SupportFragment;


public class CurrentListAdapter extends RecyclerView.Adapter<CurrentListAdapter.ViewHolder> {

    private Context context;
    private List<CurrentBooking> bookingList;
    private GetCurrentPosition getCurrentPosition;
    private ViewProfilePosition viewProfilePosition;
    private GetCallPosition callPosition;
    private GetPosition getPosition;

    public CurrentListAdapter(Context context, List<CurrentBooking> bookingList, GetCurrentPosition getCurrentPosition, ViewProfilePosition viewProfilePosition,GetCallPosition callPosition, GetPosition getPosition) {
        this.context = context;
        this.bookingList = bookingList;
        this.getCurrentPosition = getCurrentPosition;
        this.viewProfilePosition = viewProfilePosition;
        this.callPosition = callPosition;
        this.getPosition = getPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.curent_adapter_layout_new, parent, false);
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

      if(bookingList.get(position).getStatus() == 7){
        if((bookingList.get(position).getPatient_lat() != null) && (bookingList.get(position).getPatient_long() !=null) && bookingList.get(position).getCaregiver_lat() !=null &&
         bookingList.get(position).getCaregiver_long() != null){
            holder.txt_distance.setVisibility(View.VISIBLE);
              double distance = Utility.distance(Double.parseDouble(bookingList.get(position).getPatient_lat()), Double.parseDouble(bookingList.get(position).getPatient_long())
                      , Double.parseDouble(bookingList.get(position).getCaregiver_lat()), Double.parseDouble(bookingList.get(position).getCaregiver_long()));
              String kms = String.format("%.1f", milesTokm(distance));
              holder.txt_distance.setText(kms +" kms");
          }else {
            holder.txt_distance.setText(R.string.cannot_fetch_distance_error);
        }
        }else {
          holder.txt_distance.setVisibility(View.GONE);
      }

        Balloon requestSentBalloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setArrowVisible(true)
                .setHeight(45)
                .setWidth(220)
                .setTextSize(10f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.8f)
                .setText("Your request has been sent successfully!")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        Balloon acceptedBalloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setArrowVisible(true)
                .setHeight(45)
                .setWidth(220)
                .setTextSize(10f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.8f)
                .setText("Your request has been accepted successfully!")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        Balloon ontheWayBalloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setArrowVisible(true)
                .setHeight(45)
                .setWidth(220)
                .setTextSize(10f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.8f)
                .setText("Caregiver is on the way!")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        Balloon completedBalloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setArrowVisible(true)
                .setHeight(45)
                .setWidth(220)
                .setTextSize(10f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.8f)
                .setText("Your request has been completed")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        int status = bookingList.get(position).getStatus();

        if(status == 1){
            holder.imgAccept.setImageDrawable(context.getDrawable(R.drawable.user_orange));
            holder.txt_accept.setTextColor(context.getColor(R.color.orange));
            holder.layout_accept.setBackground(context.getDrawable(R.drawable.orange_circle));

            holder.imgOnWay.setImageDrawable(context.getDrawable(R.drawable.ic_walk_orange));
            holder.txt_onway.setTextColor(context.getColor(R.color.orange));
            holder.layout_onway.setBackground(context.getDrawable(R.drawable.orange_circle));

            holder.img_complete.setImageDrawable(context.getDrawable(R.drawable.ic_request_red));
            holder.txt_complete.setTextColor(context.getColor(R.color.orange));
            holder.layout_complete.setBackground(context.getDrawable(R.drawable.orange_circle));

            requestSentBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was sent successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            acceptedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be accepted.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            ontheWayBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be accepted.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            completedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be accepted.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
        }

        if(status == 3){

            holder.imgOnWay.setImageDrawable(context.getDrawable(R.drawable.user_orange));
            holder.txt_onway.setTextColor(context.getColor(R.color.orange));
            holder.layout_onway.setBackground(context.getDrawable(R.drawable.orange_circle));

            holder.img_complete.setImageDrawable(context.getDrawable(R.drawable.ic_request_red));
            holder.txt_complete.setTextColor(context.getColor(R.color.orange));
            holder.layout_complete.setBackground(context.getDrawable(R.drawable.orange_circle));

            requestSentBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was sent successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            acceptedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request has been accepted successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            ontheWayBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Caregiver is on the way.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            completedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be completed.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
        }

        if(status == 7){

            holder.img_complete.setImageDrawable(context.getDrawable(R.drawable.ic_request_red));
            holder.txt_complete.setTextColor(context.getColor(R.color.orange));
            holder.layout_complete.setBackground(context.getDrawable(R.drawable.orange_circle));

            requestSentBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was sent successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            acceptedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was accepted successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            ontheWayBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Caregiver is in contact with you.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            completedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be completed.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
        }

        if(status == 8){

            holder.img_complete.setImageDrawable(context.getDrawable(R.drawable.ic_request_red));
            holder.txt_complete.setTextColor(context.getColor(R.color.orange));
            holder.layout_complete.setBackground(context.getDrawable(R.drawable.orange_circle));

            requestSentBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was sent successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            acceptedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was accepted successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            ontheWayBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Caregiver has arrived.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            completedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is yet to be completed.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_orange))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
        }

        if(status == 2){
           // do nothing

            requestSentBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was sent successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            acceptedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request was accepted successfully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            ontheWayBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("You've already met the caregiver.")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();

            completedBalloon = new Balloon.Builder(context)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.LEFT)
                    .setArrowVisible(true)
                    .setHeight(45)
                    .setWidth(220)
                    .setTextSize(10f)
                    .setArrowPosition(0.62f)
                    .setCornerRadius(4f)
                    .setAlpha(0.8f)
                    .setText("Your request is completed successully!")
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.ensan_soft_green))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
        }

        Balloon finalRequestSentBalloon = requestSentBalloon;
        holder.requestSentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalRequestSentBalloon.showAlignRight(holder.requestSentLayout,0,-10);
            }
        });

        Balloon finalAcceptedBalloon = acceptedBalloon;
        holder.acceptedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAcceptedBalloon.showAlignRight(holder.acceptedLayout,0,-10);
            }
        });

        Balloon finalOntheWayBalloon = ontheWayBalloon;
        holder.onTheWayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalOntheWayBalloon.showAlignRight(holder.onTheWayLayout,0,-10);
            }
        });

        Balloon finalCompletedBalloon = completedBalloon;
        holder.completedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCompletedBalloon.showAlignRight(holder.completedLayout,0,-10);
            }
        });

        holder.txt_profile.setOnClickListener(v -> {
            viewProfilePosition.getOpenProfile(position);
        });

        holder.txt_cancel.setOnClickListener(v -> {
             getCurrentPosition.getPosition(position);
        });

        holder.img_call.setOnClickListener(v -> {
            callPosition.call(bookingList.get(position).getPhone());
        });

        holder.mainLayout.setOnClickListener(v -> getPosition.getPatientPosition(position,"current"));
        holder.txt_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                SupportFragment myFragment = new SupportFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mainLayout;
        private LinearLayout cancelLayout, requestSentLayout,acceptedLayout,onTheWayLayout,completedLayout;
        private CircleImageView userImg;
        private TextView txt_username, txt_profile, txt_complete;
        private TextView txt_cat, txt_price, txt_date, txt_cancel, txt_accept, txt_onway, txt_support, txt_distance;
        private ImageView imgAccept, imgOnWay, img_complete ,img_call;
        private RelativeLayout layout_accept, layout_onway, layout_complete;

        ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);
            userImg = itemView.findViewById(R.id.userImg);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_cat = itemView.findViewById(R.id.txt_cat);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_profile = itemView.findViewById(R.id.txt_profile);
            txt_cancel = itemView.findViewById(R.id.txt_cancel);
            imgAccept = itemView.findViewById(R.id.imgAccept);
            imgOnWay = itemView.findViewById(R.id.imgOnWay);
            img_call = itemView.findViewById(R.id.img_call);
            img_complete = itemView.findViewById(R.id.img_complete);
            txt_accept = itemView.findViewById(R.id.txt_accept);
            txt_complete = itemView.findViewById(R.id.txt_complete);
            txt_onway = itemView.findViewById(R.id.txt_onway);
            layout_accept = itemView.findViewById(R.id.layout_accept);
            layout_onway = itemView.findViewById(R.id.layout_onway);
            layout_complete = itemView.findViewById(R.id.layout_complete);
            requestSentLayout = itemView.findViewById(R.id.requestSentLayout);
            acceptedLayout = itemView.findViewById(R.id.acceptedLayout);
            onTheWayLayout = itemView.findViewById(R.id.onTheWayLayout);
            completedLayout = itemView.findViewById(R.id.completedLayout);
            txt_support = itemView.findViewById(R.id.txt_support);
            txt_distance = itemView.findViewById(R.id.txt_distance);
        }
    }

    // remove item from list
    public  void removeDataFromList(CurrentBooking booking){

        if(bookingList == null){
            bookingList = new ArrayList<>();
        }

        bookingList.remove(booking);
        notifyDataSetChanged();
    }


    private static double milesTokm(double distanceInMiles) {
        return distanceInMiles * 1.60934;
    }

}

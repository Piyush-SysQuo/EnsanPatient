package ensan.patientapp.view.activity.familymember.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;


public class AddFamilyDetailActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {


    @BindView(R.id.manLayout)
    RelativeLayout manLayout;

    @BindView(R.id.womenLayout)
    RelativeLayout womenLayout;


    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;

    @BindView(R.id.maleIv)
    ImageView maleIv;

    @BindView(R.id.femaleIv)
    ImageView femaleIv;

    String[] country = { "Nationality","India", "USA", "China", "Japan", "Other"};
    String[] marritalStatus = { "Marrital Status","Single","Married", "Divorced"};
    private String token;
    private String gender;
    private String fName;
    private String lName;
    private String dob;
    private String relation;
    private String type;
    private Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_detail);

        // bind data to butter_knife
        ButterKnife.bind(this);
        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
                fName = (String) bundle.get(Constants.FIRST_NAME);
                lName = (String) bundle.get(Constants.KEY_FAMILY_NAME);
                dob = (String) bundle.get(Constants.KEY_DOB);
                relation = (String) bundle.get(Constants.KEY_RELATION);
                type = (String) bundle.get(Constants.KEY_TYPE);
            }
        }


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(aa);


        ArrayAdapter statusAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,marritalStatus);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });
        mAlert.show();
    }
    public void womenClick(View view) {
        gender = "2";
        womenLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        manLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        maleIv.setVisibility(View.GONE);
        femaleIv.setVisibility(View.VISIBLE);
    }

    public void maleClick(View view) {
        gender = "1";
        womenLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        manLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        maleIv.setVisibility(View.VISIBLE);
        femaleIv.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void backPressed(View view) {
        finish();
    }



}
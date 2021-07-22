package ensan.patientapp.view.activity.familymember.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.familymember.adapter.DropDownFamilyAdapter;
import ensan.patientapp.view.activity.familymember.adapter.MemberListAdapter;
import ensan.patientapp.view.activity.familymember.model.Datum;
import ensan.patientapp.view.activity.familymember.model.GetFamilyPosition;
import ensan.patientapp.view.activity.familymember.viewmodel.DeleteFamilyMemberViewModel;
import ensan.patientapp.view.activity.familymember.viewmodel.FamilyMemberViewModel;
import ensan.patientapp.view.activity.familymember.viewmodel.MemberListViewModel;
import ensan.patientapp.view.activity.login.model.LoginResponse;

public class FamilyMemberActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, GetFamilyPosition {

    private TextView dob;
    private String token;
    private Progress progress;
    private List<Datum> list = new ArrayList<>();
    private MemberListAdapter memberListAdapter;
    private RecyclerView member_list;
    private LoginResponse resp;
    private String language;
    private LinearLayout no_family_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.family_member_activity);

        // find id's
        dob = findViewById(R.id.dob);
        member_list = findViewById(R.id.member_list);
        no_family_layout = findViewById(R.id.no_family_layout);


        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();


        // set default adapter to recycle view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        member_list.setLayoutManager(mLayoutManager);
        memberListAdapter = new MemberListAdapter(list,this);
        member_list.setAdapter(memberListAdapter);

        // get family list
         getFamilyMemberList();

        // select dob
        dob.setOnClickListener(v -> {
            DialogFragment datePicker = new ensan.patientapp.Utils.DatePicker();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        if (list==null || list.size()==0){
            member_list.setVisibility(View.GONE);
        }


    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }


    // open success dialog
    private void openSuccessDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> mAlert.dismiss());
        mAlert.show();
    }


    public void requestSentClick(View view) {
        Intent intent = new Intent(this, AddFamilyMemberActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();

        // get family member
        getFamilyMemberList();
    }

    public void backPressed(View view) {
        finish();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dob.setText(currentDateString);

    }


    @Override
    public void getPosition(int position) {
        Intent intent = new Intent(this, FamilyMedicalHistoryActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID,list.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void deletePosition(int position) {
        progress.show();
        DeleteFamilyMemberViewModel viewModel = ViewModelProviders.of(this).get(DeleteFamilyMemberViewModel.class);
        viewModel.init();
        viewModel.deleteFamilyMember(token,String.valueOf(list.get(position).getId()));
        viewModel.getVolumesResponseLiveData().observe(this, familyMemberResponse -> {
              progress.hide();
              if (familyMemberResponse != null){
                  if(familyMemberResponse.getSuccess()){
                      openSuccessDialog(familyMemberResponse.getMessage());
                      memberListAdapter.removeMember(list.get(position));
                      if(list.size() == 0){
                          no_family_layout.setVisibility(View.VISIBLE);
                          member_list.setVisibility(View.GONE);
                      }
                  }else{
                      openDialog(familyMemberResponse.getMessage());
                  }
              }else{
                  openDialog(getString(R.string.errormsg));
              }
        });
    }

    public void btnContinue(View view) {
        finish();
    }


    // call family list api
    private synchronized void getFamilyMemberList(){
        progress.show();
        MemberListViewModel memberListViewModel = ViewModelProviders.of(this).get(MemberListViewModel.class);
        memberListViewModel.init();
        memberListViewModel.getFamilyMember(token);
        memberListViewModel.getVolumesResponseLiveData().observe(this, familyMemberListResponse -> {
            progress.hide();
            if(familyMemberListResponse.getStatus()){
                list =  familyMemberListResponse.getData();
                memberListAdapter.setMemebers(list);
                if(list.size() == 0){
                    no_family_layout.setVisibility(View.VISIBLE);
                    member_list.setVisibility(View.GONE);
                }else {
                    no_family_layout.setVisibility(View.GONE);
                    member_list.setVisibility(View.VISIBLE);
                }
            }else {
                // do nothing
            }
        });
    }
}
package ensan.patientapp.view.activity.choosedatetime.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.parceler.Parcels;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.databinding.ChooseDateTimeActivityBinding;
import ensan.patientapp.view.activity.bookingdetails.view.BookingDetailsActivity;
import ensan.patientapp.view.activity.choosedatetime.adapter.DropDownTimeAdapter;
import ensan.patientapp.view.activity.choosedatetime.adapter.SelectedServiceListAdapter;
import ensan.patientapp.view.activity.choosedatetime.model.LanguageDatum;
import ensan.patientapp.view.activity.choosedatetime.viewmodel.FrequencyListViewModel;
import ensan.patientapp.view.activity.choosedatetime.viewmodel.LanguagesViewModel;
import ensan.patientapp.view.activity.familymember.viewmodel.MemberListViewModel;
import ensan.patientapp.view.activity.home.model.Datum;
import ensan.patientapp.view.activity.login.model.LoginResponse;


public class ChooseDateTimeActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    ChooseDateTimeActivityBinding chooseDateTimeActivityBinding;
    private String token;
    private List<Datum> datumList;
    private List<LanguageDatum> languageDatumList = new ArrayList<>();
    private String[] spinnerArray = new String[languageDatumList.size()];
    private List<ensan.patientapp.view.activity.choosedatetime.model.Datum> frequencyArrayList = new ArrayList<>();
    private List<ensan.patientapp.view.activity.familymember.model.Datum> familyMemberList = new ArrayList<>();
    private String[] familyArray = new String[familyMemberList.size()];
    private String[] frequencyArray = new String[frequencyArrayList.size()];
    private HashMap<Integer,String> spinnerMap = new HashMap<>();
    private HashMap<Integer,String> frequencyMap = new HashMap<>();
    private HashMap<Integer,String> familyMap = new HashMap<>();
    private boolean isFrom;
    private Progress progress;
    private String subCatID;
    private int catID;
    private boolean isSelect;
    private String bookingFor = "1";
    private  int frequencyPos;
    private int languagePos;
    private int familyPos;
    private LoginResponse resp;
    private String language;
    private SelectedServiceListAdapter selectedServiceListAdapter;

    private JSONArray subCatIDArray = new JSONArray();


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton yes;

    @BindView(R.id.radioButton2)
    RadioButton no;

    @BindView(R.id.servicesRV)
    RecyclerView servicesRV;

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

        chooseDateTimeActivityBinding= DataBindingUtil.setContentView(this, R.layout.choose_date_time_activity);

        ButterKnife.bind(this);

         // initialize progress dialog instance
         progress = new Progress(this);
         (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         progress.setCanceledOnTouchOutside(false);
         progress.setCancelable(false);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                datumList = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_DATA));
                catID = (int) bundle.get(Constants.KEY_CAT_ID);
            }
        }

        chooseDateTimeActivityBinding.imgBack.setOnClickListener(this);
        chooseDateTimeActivityBinding.imgCross.setOnClickListener(this);
        chooseDateTimeActivityBinding.selectTimeLayout.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        yes.setOnCheckedChangeListener(this);
        no.setOnCheckedChangeListener(this);

        if(datumList != null){
            chooseDateTimeActivityBinding.servicesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            selectedServiceListAdapter = new SelectedServiceListAdapter(this,datumList);
            chooseDateTimeActivityBinding.servicesRV.setAdapter(selectedServiceListAdapter);

            for(int i = 0; i< datumList.size(); i++){
                subCatIDArray.put(datumList.get(i).getId());
            }

        }

        // get all family member
         getFamilyMember();

        // call frequency list API
         getFrequencyList();

        // get all languages list API
         getLanguagesList();

         // from date button clicked
         chooseDateTimeActivityBinding.layoutFromDate.setOnClickListener(v -> {
             if(frequencyPos == 0){
                 openDialog(getString(R.string.frequency_error));
                 return;
             }
             isFrom = true;
             DialogFragment datePicker = new ensan.patientapp.Utils.DatePicker();
             datePicker.show(getSupportFragmentManager(), "date picker");
         });


         chooseDateTimeActivityBinding.frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 frequencyPos = position;
                 if(frequencyArrayList.size()>0) {
                     String name = frequencyArrayList.get(position).getArName();
                     if (name.equals(getString(R.string.single_visit))) {
                         chooseDateTimeActivityBinding.layouttodate.setVisibility(View.INVISIBLE);
                     }else{
                         chooseDateTimeActivityBinding.layouttodate.setVisibility(View.VISIBLE);
                     }
                 }
                 chooseDateTimeActivityBinding.txtFrom.setText(getString(R.string.from_date));
                 chooseDateTimeActivityBinding.txtTo.setText(getString(R.string.to_date));
                 chooseDateTimeActivityBinding.txtTime.setText(getString(R.string.select_time));
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         chooseDateTimeActivityBinding.familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 familyPos = position;
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         chooseDateTimeActivityBinding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 languagePos = position;
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

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

    private void getFamilyMember() {
         progress.show();
        // call member list API
        MemberListViewModel memberListViewModel = ViewModelProviders.of(this).get(MemberListViewModel.class);
        memberListViewModel.init();
        memberListViewModel.getFamilyMember(token);
        memberListViewModel.getVolumesResponseLiveData().observe(this, familyMemberListResponse -> {
            progress.hide();
            if(familyMemberListResponse.getStatus()){
                familyMemberList =  familyMemberListResponse.getData();
                familyArray = new String[familyMemberList.size()];
                if(familyMemberList.size() == 0 ){
                    bookingFor = "1";
                }
                for (int i = 0; i < familyMemberList.size(); i++)
                {
                    familyMap.put(i,String.valueOf(familyMemberList.get(i).getId()));
                    familyArray[i] = familyMemberList.get(i).getName();
                }

                DropDownTimeAdapter adapter =new DropDownTimeAdapter(ChooseDateTimeActivity.this, Arrays.asList(familyArray));
                chooseDateTimeActivityBinding.familySpinner.setAdapter(adapter);
            }else {
                // do nothing
            }
        });
    }

    private void getLanguagesList() {
         progress.show();
        LanguagesViewModel languagesViewModel = ViewModelProviders.of(this).get(LanguagesViewModel.class);
        languagesViewModel.init();
        languagesViewModel.getLanguages(token,language);
        languagesViewModel.getVolumesResponseLiveData().observe(this, languagesResponse -> {
            progress.hide();
            if(languagesResponse.getStatus()){
                 languageDatumList = languagesResponse.getData();
                 spinnerArray = new String[languageDatumList.size()];
                 for (int i = 0; i < languageDatumList.size(); i++)
                 {
                    spinnerMap.put(i,languageDatumList.get(i).getValue());
                    spinnerArray[i] = languageDatumList.get(i).getLabel();
                 }
                DropDownTimeAdapter adapter =new DropDownTimeAdapter(ChooseDateTimeActivity.this,Arrays.asList(spinnerArray));
                chooseDateTimeActivityBinding.languageSpinner.setAdapter(adapter);
            }else{
                // do nothing
            }
        });
    }

    private void getFrequencyList() {
         progress.show();
        FrequencyListViewModel frequencyListViewModel = ViewModelProviders.of(this).get(FrequencyListViewModel.class);
        frequencyListViewModel.init();
        frequencyListViewModel.getFrequency(token);
        frequencyListViewModel.getVolumesResponseLiveData().observe(this, frequencyResponse -> {
            progress.hide();
             if(frequencyResponse.getSuccess()){
                 frequencyArrayList = frequencyResponse.getData();
                 frequencyArray = new String[frequencyArrayList.size()];
                 for (int i = 0; i < frequencyArrayList.size(); i++)
                 {
                     frequencyMap.put(i,frequencyArrayList.get(i).getId().toString());
                     frequencyArray[i] = frequencyArrayList.get(i).getArName();
                 }
                 DropDownTimeAdapter adapter =new DropDownTimeAdapter(ChooseDateTimeActivity.this,Arrays.asList(frequencyArray));
                 chooseDateTimeActivityBinding.frequencySpinner.setAdapter(adapter);
             }else{
                 // do nothing
             }
        });
    }


    public void nextClick(View view) {
         // get language, frequency & family member spinner id
        String frequencyId = frequencyMap.get(frequencyPos);
        String languageId = spinnerMap.get(languagePos);
        String familyId = familyMap.get(familyPos);
        if(!isSelect){
            familyId = "0";
        }
        String frequency = frequencyArray[frequencyPos];
     //   String frequency = (String) chooseDateTimeActivityBinding.frequencySpinner.getSelectedItem();
        if(frequency.equals("Please select frequency") || frequency.equals("الرجاء تحديد التردد")){
            Toast.makeText(this,getString(R.string.freqerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(chooseDateTimeActivityBinding.txtFrom.getText().toString().equals(getString(R.string.from_date))){
            Toast.makeText(this,getString( R.string.dateeror), Toast.LENGTH_SHORT).show();
            return;
        }else if(chooseDateTimeActivityBinding.txtTime.getText().toString().equals(getString(R.string.select_time))){
            Toast.makeText(this, getString(R.string.timeerror), Toast.LENGTH_SHORT).show();
            return;
        }else {
            /*Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
            intent.putExtra(Constants.KEY_TOKEN, token);
            intent.putExtra(Constants.KEY_FREQUENCY_ID, frequencyId);
            intent.putExtra(Constants.KEY_LANGUAGE_ID, languageId);
            intent.putExtra(Constants.KEY_CAT_ID, catID);
//            intent.putExtra(Constants.KEY_SUB_CAT_ID, Parcels.wrap(subCatIDArray));
            intent.putExtra(Constants.KEY_SUB_CAT_ID, subCatIDArray.toString());
            intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID, familyId);
            intent.putExtra(Constants.KEY_BOOKING_FOR, bookingFor);
            intent.putExtra(Constants.KEY_FROM_DATE, chooseDateTimeActivityBinding.txtFrom.getText().toString());
            intent.putExtra(Constants.KEY_TO_DATE, chooseDateTimeActivityBinding.txtTo.getText().toString());
            intent.putExtra(Constants.KEY_TIME, chooseDateTimeActivityBinding.txtTime.getText().toString());
            startActivity(intent);*/
            try {
                SimpleDateFormat timeStampFormatCrnt = new SimpleDateFormat("MMM dd, yyyy");
                Date myDateCrnt = new Date();
                String CrntDate = timeStampFormatCrnt.format(myDateCrnt);
                Date CurrenttDate = timeStampFormatCrnt.parse(CrntDate);


                String dtSelect = chooseDateTimeActivityBinding.txtFrom.getText().toString();
                Date SelectDate = timeStampFormatCrnt.parse(dtSelect);


                String string1 = chooseDateTimeActivityBinding.txtTime.getText().toString();
                Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String someRandomTime = sdf.format(new Date());
                Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date Crnt = calendar3.getTime();
                Date Selct = calendar1.getTime();


                if(Selct.before(Crnt) && SelectDate.after(CurrenttDate) == false)
                {
//                    Toast.makeText(this, getString(R.string.incorrecttimeerror), Toast.LENGTH_SHORT).show();
                    openDialog(getString(R.string.incorrecttimeerror));
                    return;
                }
                else if(Selct.before(Crnt) && SelectDate.after(CurrenttDate) == true )
                {
                    Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                    intent.putExtra(Constants.KEY_TOKEN, token);
                    intent.putExtra(Constants.KEY_FREQUENCY_ID, frequencyId);
                    intent.putExtra(Constants.KEY_LANGUAGE_ID, languageId);
                    intent.putExtra(Constants.KEY_CAT_ID, catID);
                    intent.putExtra(Constants.KEY_SUB_CAT_ID, subCatIDArray.toString());
                    intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID, familyId);
                    intent.putExtra(Constants.KEY_BOOKING_FOR, bookingFor);
                    intent.putExtra(Constants.KEY_FROM_DATE, chooseDateTimeActivityBinding.txtFrom.getText().toString());
                    intent.putExtra(Constants.KEY_TO_DATE, chooseDateTimeActivityBinding.txtTo.getText().toString());
                    intent.putExtra(Constants.KEY_TIME, chooseDateTimeActivityBinding.txtTime.getText().toString());
                    startActivity(intent);

                }
                else if(Selct.equals(Crnt))
                {
                    Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                    intent.putExtra(Constants.KEY_TOKEN, token);
                    intent.putExtra(Constants.KEY_FREQUENCY_ID, frequencyId);
                    intent.putExtra(Constants.KEY_LANGUAGE_ID, languageId);
                    intent.putExtra(Constants.KEY_CAT_ID, catID);
                    intent.putExtra(Constants.KEY_SUB_CAT_ID, subCatIDArray.toString());
                    intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID, familyId);
                    intent.putExtra(Constants.KEY_BOOKING_FOR, bookingFor);
                    intent.putExtra(Constants.KEY_FROM_DATE, chooseDateTimeActivityBinding.txtFrom.getText().toString());
                    intent.putExtra(Constants.KEY_TO_DATE, chooseDateTimeActivityBinding.txtTo.getText().toString());
                    intent.putExtra(Constants.KEY_TIME, chooseDateTimeActivityBinding.txtTime.getText().toString());
                    startActivity(intent);
                }
                else if(Selct.after(Crnt))
                {
                    Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                    intent.putExtra(Constants.KEY_TOKEN, token);
                    intent.putExtra(Constants.KEY_FREQUENCY_ID, frequencyId);
                    intent.putExtra(Constants.KEY_LANGUAGE_ID, languageId);
                    intent.putExtra(Constants.KEY_CAT_ID, catID);
                    intent.putExtra(Constants.KEY_SUB_CAT_ID, subCatIDArray.toString());
                    intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID, familyId);
                    intent.putExtra(Constants.KEY_BOOKING_FOR, bookingFor);
                    intent.putExtra(Constants.KEY_FROM_DATE, chooseDateTimeActivityBinding.txtFrom.getText().toString());
                    intent.putExtra(Constants.KEY_TO_DATE, chooseDateTimeActivityBinding.txtTo.getText().toString());
                    intent.putExtra(Constants.KEY_TIME, chooseDateTimeActivityBinding.txtTime.getText().toString());
                    startActivity(intent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_cross:
            case R.id.img_back:
                finish();
                break;
            case R.id.select_time_layout :
                if(chooseDateTimeActivityBinding.txtFrom.getText().toString().equals(getString(R.string.from_date)))
                {
                    openDialog(getString(R.string.dateeror));
                }
                else {
                    showClock(chooseDateTimeActivityBinding.txtTime);
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, year);
        c2.set(Calendar.MONTH, month);
        c2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        String toDateString = "";
        if (frequencyPos==1){
            toDateString = currentDateString;
        }else if (frequencyPos==2){
            c2.add(Calendar.DATE,6);
            toDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c2.getTime());
        }else if (frequencyPos==3){
            c2.add(Calendar.DATE,29);
            toDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c2.getTime());
        }else if (frequencyPos==4){
            c2.add(Calendar.DATE,6);
            toDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c2.getTime());
        }
        showDate(currentDateString);
        isFrom = false;
        showDate(toDateString);
    }

    // show date
    private void showDate(String selectedDate){
         if(isFrom) {
             chooseDateTimeActivityBinding.txtFrom.setText(selectedDate);
         }else{
             chooseDateTimeActivityBinding.txtTo.setText(selectedDate);
         }
    }

    // show open clock
    private void showClock(TextView textView){

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog, (timePicker, selectedHour, selectedMinute) -> {

            try{
                SimpleDateFormat timeStampFormatCrnt = new SimpleDateFormat("MMM dd, yyyy");
                Date myDateCrnt = new Date();
                String CrntDate = timeStampFormatCrnt.format(myDateCrnt);
                Date CurrenttDate = timeStampFormatCrnt.parse(CrntDate);

                String dtSelect = chooseDateTimeActivityBinding.txtFrom.getText().toString();
                Date SelectDate = timeStampFormatCrnt.parse(dtSelect);

                if(SelectDate.after(CurrenttDate) == false)
                {
                    int min = timePicker.getMinute();
                    int hr = timePicker.getHour();
                    String strCurrentTime = hour+":"+minute;
                    DateFormat dateCurrentFormat = new SimpleDateFormat("HH:mm");
                    Date CurrentTime = dateCurrentFormat.parse(strCurrentTime);

                    String strSelectTime = hr+":"+min;
                    DateFormat dateSelectFormat = new SimpleDateFormat("HH:mm");
                    Date SelectTime = dateSelectFormat.parse(strSelectTime);

//                    if( ( hr >= hour ) && (  min >= minute ))
                    if( SelectTime.after(CurrentTime) || SelectTime.equals(CurrentTime))
                    {
                        /*if(  min >= minute )
                        {
                            min = timePicker.getMinute();
                            hr = timePicker.getHour();
                            if(min<10){
                                textView.setText( hr + ":" +"0"+ min);
                            }else {
                                textView.setText( hr + ":" + min);
                            }
                        }
                        else
                        {
                            //it's before current'
                            chooseDateTimeActivityBinding.txtTime.setText(getString(R.string.select_time));
                            openDialog(getString(R.string.incorrecttimeerror));
                        }*/
                        min = timePicker.getMinute();
                        hr = timePicker.getHour();
                        if(min<10){
                            textView.setText( hr + ":" +"0"+ min);
                        }else {
                            textView.setText( hr + ":" + min);
                        }

                    } else {
                        //it's before current'
                        chooseDateTimeActivityBinding.txtTime.setText(getString(R.string.select_time));
                        openDialog(getString(R.string.incorrecttimeerror));
                    }
                }
                else if(SelectDate.after(CurrenttDate) == true )
                {
                    int min = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        min = timePicker.getMinute();
                        int hr = timePicker.getHour();
                        if(min<10){
                            textView.setText( hr + ":" +"0"+ min);
                        }else {
                            textView.setText( hr + ":" + min);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }, hour, minute, true);//Yes 12 hour time
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mTimePicker.show();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioButton1 :
                isSelect = true;
                bookingFor = "2";
                chooseDateTimeActivityBinding.familySpinner.setVisibility(View.VISIBLE);
                chooseDateTimeActivityBinding.view.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButton2:
                isSelect = false;
                bookingFor = "1";
                chooseDateTimeActivityBinding.familySpinner.setVisibility(View.GONE);
                chooseDateTimeActivityBinding.view.setVisibility(View.GONE);
                break;
        }
    }

    public void backPressed(View view) {
         finish();
    }

}
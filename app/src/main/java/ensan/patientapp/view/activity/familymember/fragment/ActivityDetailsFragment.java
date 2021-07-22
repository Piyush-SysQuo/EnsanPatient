package ensan.patientapp.view.activity.familymember.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.caregiverprofile.adapter.LanguageAdapter;
import ensan.patientapp.view.activity.familymember.view.FamilyMedicalHistoryActivity;
import ensan.patientapp.view.activity.medicalhistory.model.Activity;


public class ActivityDetailsFragment extends Fragment {

    private List<Activity> activityList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityDetailsFragment() {
        // Required empty public constructor
    }

    public ActivityDetailsFragment(List<Activity> activityList) {
        if(activityList == null){
            activityList = new ArrayList<>();
        }
        this.activityList = activityList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView medicalRv = getView().findViewById(R.id.medicalRv);
        LinearLayout activity_Layout = getView().findViewById(R.id.activity_Layout);
        LinearLayout no_activity_layout = getView().findViewById(R.id.no_activity_layout);
        TextView btnContinue = getView().findViewById(R.id.btnContinue);


        if (activityList.size() == 0) {
              activity_Layout.setVisibility(View.GONE);
              no_activity_layout.setVisibility(View.VISIBLE);
        } else {
            activity_Layout.setVisibility(View.VISIBLE);
            no_activity_layout.setVisibility(View.GONE);
            List<String> activities = new ArrayList<>();
            if (activityList.get(0).getSmoke().matches("yes")){
                activities.add(getString(R.string.smoke));
            }
            if (activityList.get(0).getExercise().matches("yes")){
                activities.add(getString(R.string.exercise));
            }
            if(activityList.get(0).getSmoke().matches("") && activityList.get(0).getExercise().matches("")){
                activity_Layout.setVisibility(View.GONE);
                no_activity_layout.setVisibility(View.VISIBLE);
            }
            medicalRv.setLayoutManager(new LinearLayoutManager(getContext()));
            LanguageAdapter allergyAdapter = new LanguageAdapter(getContext(),activities);
            medicalRv.setAdapter(allergyAdapter);
        }

        // continue button clicked
        btnContinue.setOnClickListener(v -> {
            getActivity().finish();
        });
    }
}
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
import ensan.patientapp.view.activity.medicalhistory.model.Employment;


public class EmploymentDetailsFragment extends Fragment {

    private List<Employment> employmentList;

    public EmploymentDetailsFragment() {
        // Required empty public constructor
    }

    public EmploymentDetailsFragment(List<Employment> employmentList) {
        if(employmentList == null){
            employmentList = new ArrayList<>();
        }
        this.employmentList = employmentList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_employment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView medicalRv = getView().findViewById(R.id.medicalRv);
        LinearLayout no_emp_layout = getView().findViewById(R.id.no_emp_layout);
        LinearLayout emp_layout = getView().findViewById(R.id.emp_layout);
        TextView btnContinue = getView().findViewById(R.id.btnContinue);


        if (employmentList.size() == 0) {

                no_emp_layout.setVisibility(View.VISIBLE);
                emp_layout.setVisibility(View.GONE);
        } else {
            List<String> empList = new ArrayList<>();
            if (employmentList.get(0).getWorking().matches("yes")){
                no_emp_layout.setVisibility(View.GONE);
                emp_layout.setVisibility(View.VISIBLE);
                empList.add(employmentList.get(0).getDetail());
                medicalRv.setLayoutManager(new LinearLayoutManager(getContext()));
                LanguageAdapter allergyAdapter = new LanguageAdapter(getContext(),empList);
                medicalRv.setAdapter(allergyAdapter);
            }else {
                no_emp_layout.setVisibility(View.VISIBLE);
                emp_layout.setVisibility(View.GONE);
            }

        }

        // continue button clicked
        btnContinue.setOnClickListener(v -> {
            getActivity().finish();
        });
    }
}
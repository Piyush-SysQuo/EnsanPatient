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
import ensan.patientapp.view.activity.medicalhistory.model.Allergy;


public class AllergyDetailsFragment extends Fragment {

    private List<Allergy> allergyList;

    public AllergyDetailsFragment() {
        // Required empty public constructor
    }

    public AllergyDetailsFragment(List<Allergy> allergyList) {
        if(allergyList == null){
            allergyList = new ArrayList<>();
        }
        this.allergyList = allergyList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allergy_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView medicalRv = getView().findViewById(R.id.medicalRv);
        LinearLayout allergy_Layout = getView().findViewById(R.id.allergy_Layout);
        LinearLayout no_medical_allergy_layout = getView().findViewById(R.id.no_medical_allergy_layout);
        TextView btnContinue = getView().findViewById(R.id.btnContinue);


        if (allergyList.size() == 0) {
            allergy_Layout.setVisibility(View.GONE);
            no_medical_allergy_layout.setVisibility(View.VISIBLE);
        } else {
            allergy_Layout.setVisibility(View.VISIBLE);
            no_medical_allergy_layout.setVisibility(View.GONE);
                List<String> allergies = new ArrayList<>();
                for (int i =0; i<allergyList.size(); i++){
                    allergies.add(allergyList.get(i).getAllergyKey() + " : " +allergyList.get(i).getValue());
                }

            medicalRv.setLayoutManager(new LinearLayoutManager(getContext()));
            LanguageAdapter allergyAdapter = new LanguageAdapter(getContext(),allergies);
            medicalRv.setAdapter(allergyAdapter);
        }

        // continue button clicked
        btnContinue.setOnClickListener(v -> {
             getActivity().finish();
        });
    }
}
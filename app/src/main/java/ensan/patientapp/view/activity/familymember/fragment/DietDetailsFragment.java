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
import ensan.patientapp.view.activity.medicalhistory.model.Diet;


public class DietDetailsFragment extends Fragment {

    private List<Diet> dietList;


    public DietDetailsFragment() {
        // Required empty public constructor
    }

    public DietDetailsFragment(List<Diet> dietList) {
        if(dietList == null){
            dietList = new ArrayList<>();
        }
        this.dietList = dietList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView medicalRv = getView().findViewById(R.id.medicalRv);
        LinearLayout no_diet_layout = getView().findViewById(R.id.no_diet_layout);
        LinearLayout diet_Layout = getView().findViewById(R.id.diet_Layout);
        TextView btnContinue = view.findViewById(R.id.btnContinue);

        if (dietList.size() == 0) {
            no_diet_layout.setVisibility(View.VISIBLE);
            diet_Layout.setVisibility(View.GONE);
        } else {
            no_diet_layout.setVisibility(View.GONE);
            diet_Layout.setVisibility(View.VISIBLE);
            List<String> diets = new ArrayList<>();
            for (int i =0; i<dietList.size(); i++){
                diets.add(dietList.get(i).getValue());
            }
            medicalRv.setLayoutManager(new LinearLayoutManager(getContext()));
            LanguageAdapter allergyAdapter = new LanguageAdapter(getContext(),diets);
            medicalRv.setAdapter(allergyAdapter);
        }

        // continue button clicked
        btnContinue.setOnClickListener(v -> {
            getActivity().finish();
        });
    }
}
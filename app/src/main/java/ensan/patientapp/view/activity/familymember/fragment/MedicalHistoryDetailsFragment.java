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
import ensan.patientapp.view.activity.familymember.view.FamilyMedicalHistoryActivity;
import ensan.patientapp.view.activity.medicalhistory.adapter.MedicalListAdapterNew;
import ensan.patientapp.view.activity.medicalhistory.model.Medical;

public class MedicalHistoryDetailsFragment extends Fragment {


    private List<Medical> medicalList;

    public MedicalHistoryDetailsFragment() {
        // Required empty public constructor
    }

    public MedicalHistoryDetailsFragment(List<Medical> medicalList) {
        if(medicalList == null){
            medicalList = new ArrayList<>();
        }
        this.medicalList = medicalList;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_history_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView medicalRv = getView().findViewById(R.id.medicalRv);
        LinearLayout no_medical_history_layout = view.findViewById(R.id.no_medical_history_layout);
        LinearLayout medicalHistoryLayout = view.findViewById(R.id.medicalHistoryLayout);
        TextView btnContinue = view.findViewById(R.id.btnContinue);

        if (medicalList.size() == 0) {
             medicalHistoryLayout.setVisibility(View.GONE);
             no_medical_history_layout.setVisibility(View.VISIBLE);
        } else {
              medicalHistoryLayout.setVisibility(View.VISIBLE);
              no_medical_history_layout.setVisibility(View.GONE);
              medicalRv.setLayoutManager(new LinearLayoutManager(getContext()));
              MedicalListAdapterNew medicalListAdapter = new MedicalListAdapterNew(medicalList, getContext());
              medicalRv.setAdapter(medicalListAdapter);
        }

        // continue button clicked
        btnContinue.setOnClickListener(v -> {
              getActivity().finish();
        });
    }

}

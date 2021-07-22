package ensan.patientapp.view.activity.personaldetail.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.view.activity.personaldetail.adapter.MaritalStatusAdapter;
import ensan.patientapp.view.activity.personaldetail.model.GetMaritalStatus;
import ensan.patientapp.view.activity.personaldetail.view.PersonalDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaritalStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaritalStatusFragment extends BottomSheetDialogFragment implements GetMaritalStatus {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String statusCode;
    private GetMaritalStatus getMaritalStatus;
    private String[] marritalStatus = new String[]{};
    private MaritalStatusAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MaritalStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenderSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaritalStatusFragment newInstance(String param1, String param2) {
        MaritalStatusFragment fragment = new MaritalStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_marital_status, container, false);
        RecyclerView statusRV = view.findViewById(R.id.status_list);
        marritalStatus = getResources().getStringArray(R.array.maritalstatus);
        if (mAdapter == null) {
            mAdapter = new MaritalStatusAdapter(marritalStatus,getContext(),this);
        }

        statusRV.setAdapter(mAdapter);
        statusRV.setLayoutManager(new LinearLayoutManager(getContext()));
        statusRV.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }
    private void onCancel(MaritalStatusFragment maritalStatusFragment) {
        maritalStatusFragment.dismiss();
    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(getContext());
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

    @Override
    public void setMaritalStatus(int position) {
        PersonalDetailsActivity.txt_marital_status.setText(marritalStatus[position+1]);
        PersonalDetailsActivity.maritalStatusCode = String.valueOf(position);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
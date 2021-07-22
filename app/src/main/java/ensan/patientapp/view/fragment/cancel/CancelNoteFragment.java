package ensan.patientapp.view.fragment.cancel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ensan.patientapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelNoteFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private CancelDatum cancelDatum;
    private String notes;

    public CancelNoteFragment() {
        // Required empty public constructor
    }

    public CancelNoteFragment(Context context,CancelDatum cancelDatum) {
        // Required empty public constructor
        this.context = context;
        this.cancelDatum = cancelDatum;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelNoteFragment newInstance(String param1, String param2) {
        CancelNoteFragment fragment = new CancelNoteFragment();
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
        View view =  inflater.inflate(R.layout.fragment_cancel_note, container, false);

      TextView continueClickBtn =  view.findViewById(R.id.continueClickBtn);
      ImageView img_cancel = view.findViewById(R.id.img_cancel);
      RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
      RadioButton rbPlan = view.findViewById(R.id.rbPlan);
      RadioButton rbCostlty = view.findViewById(R.id.rbCostlty);
      RadioButton rbCregiver = view.findViewById(R.id.rbCregiver);
      RadioButton rbMyreason = view.findViewById(R.id.rbMyreason);

    /*  EditText etNote =  view.findViewById(R.id.etNote);
        continueClickBtn.setOnClickListener(v -> {
           String note =  etNote.getText().toString().trim();
           if(note.isEmpty()){
               Toast.makeText(context,getString( R.string.cancelerror), Toast.LENGTH_SHORT).show();
           }else {
               this.dismiss();
               cancelDatum.notes(note);
           }

        });*/


        continueClickBtn.setOnClickListener(v -> {
            if(notes.isEmpty()){
                Toast.makeText(context,getString( R.string.cancelerror), Toast.LENGTH_SHORT).show();
            }else {
                this.dismiss();
                cancelDatum.notes(notes);
            }
        });



        img_cancel.setOnClickListener(v -> {
               this.dismiss();
        });

        rbPlan.setOnClickListener(v -> {
            notes = rbPlan.getText().toString();
        });

        rbCostlty.setOnClickListener(v -> {
           notes =  rbCostlty.getText().toString();
        });

        rbCregiver.setOnClickListener(v -> {
           notes = rbCregiver.getText().toString();
        });

        rbMyreason.setOnClickListener(v -> {
           notes = rbMyreason.getText().toString();
        });


        return view;
    }
}
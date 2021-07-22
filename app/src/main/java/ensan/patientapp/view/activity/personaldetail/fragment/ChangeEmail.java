package ensan.patientapp.view.activity.personaldetail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ensan.patientapp.R;
import ensan.patientapp.view.fragment.setting.model.GetEmailData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeEmail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeEmail extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private GetEmailData getEmailData;

    public ChangeEmail() {
        // Required empty public constructor
    }

    public ChangeEmail(Context context, GetEmailData getEmailData) {
        // Required empty public constructor
        this.context = context;
        this.getEmailData = getEmailData;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeEmail.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeEmail newInstance(String param1, String param2) {
        ChangeEmail fragment = new ChangeEmail();
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
        View view =  inflater.inflate(R.layout.fragment_change_email, container, false);
        TextView continueClickBtn = view.findViewById(R.id.continueClickBtn);
        ImageView closeDialog = view.findViewById(R.id.img_cancel);
        EditText etEmail = view.findViewById(R.id.etEmail);

        closeDialog.setOnClickListener(v ->{
            onCancel(this);
        });

        continueClickBtn.setOnClickListener(v -> {
                String emailId =  etEmail.getText().toString().trim();
                if(emailId.isEmpty()){
                    Toast.makeText(context, getString(R.string.emailerror), Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
                    Toast.makeText(context, getString(R.string.emailerror), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getEmailData.getEmail(emailId);
                    onCancel(this);
                }
        });
        return view;
    }

    // dismiss dialog
    private void onCancel(ChangeEmail changeEmail) {
        changeEmail.dismiss();
    }
}
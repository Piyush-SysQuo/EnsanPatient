package ensan.patientapp.view.fragment.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;


public class SupportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.support_fragment, container, false);

        ImageView imageView = view.findViewById(R.id.backBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        LinearLayout emailLayout = view.findViewById(R.id.emailLayout);
        LinearLayout callLayout = view.findViewById(R.id.supportCallLayout);
        LinearLayout whatsappLayout = view.findViewById(R.id.whatsAppLayout);
        LinearLayout userManualLayout = view.findViewById(R.id.userManualWebLayout);

        emailLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Constants.SUPPORT_EMAIL });
            intent.putExtra(Intent.EXTRA_SUBJECT, Constants.SUPPORT_SUBJECT);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }else{
                Toast.makeText(getContext(), getString(R.string.no_apps_error), Toast.LENGTH_SHORT).show();
            }
        });

        callLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Constants.SUPPORT_PHONE, null));
            startActivity(intent);
        });

        whatsappLayout.setOnClickListener(v -> {
            long number = +966126591191L;
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + "Hi";
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(sendIntent);
            }else{
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }
        });

        userManualLayout.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SUPPORT_MANUAL_URL));
            if (browserIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(browserIntent);
            }else{
                Toast.makeText(getContext(), getString(R.string.no_apps_error), Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
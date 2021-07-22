package ensan.patientapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.login.model.LoginResponse;

public class SuccessPopup extends Dialog {

    private String message;
    private View.OnClickListener btYesListener=null;
    private Context context;

    public SuccessPopup(Context context) {
        super(context);
        this.context = context;
    }

    public SuccessPopup(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected SuccessPopup(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(context).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            String language = resp.getData().getLanaguage();
            Utility.setLocale((Activity) context, language);
        }

        setContentView(R.layout.success_dialog);

        TextView tvmessage = (TextView) findViewById(R.id.message);
        tvmessage.setText(getMessage());
        Button btok = (Button) findViewById(R.id.btok);
        btok.setOnClickListener(btYesListener);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOkButton( View.OnClickListener onClickListener) {
        dismiss();
        this.btYesListener = onClickListener;

    }

}
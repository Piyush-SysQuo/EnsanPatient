package ensan.patientapp.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ensan.patientapp.R;

public class AlertPopup extends Dialog {

    private String message;
    private View.OnClickListener btYesListener = null;

    public AlertPopup(Context context) {
        super(context);
    }

    public AlertPopup(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AlertPopup(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warn_dialog);

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
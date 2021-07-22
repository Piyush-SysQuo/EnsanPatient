package ensan.patientapp.view.activity.paymentMethod.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;

public class AddPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
    }

    public void nextClick(View view) {
        finish();
    }

    public void backPressed(View view) {
        finish();
    }
}
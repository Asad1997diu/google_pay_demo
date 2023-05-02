package com.panda.googlepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.panda.googlepay.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user&hl=en&gl=US";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount;
    String name = "Md Asadujjaman";
    String upiId  = "asad123@done" ;
    String transactionNote = "pay test";
    String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount =binding.amountEditText.getText().toString();
                if (!amount.isEmpty()){
                    uri = getUpiPaymentUri(name, upiId, transactionNote,amount);
                    payWithGpay();
                }
                else {
                    binding.amountEditText.setError("Amount is required!");
                    binding.amountEditText.requestFocus();
                }

            }
        });

    }

    private void payWithGpay() {
        if (isAppInstalled( this, GOOGLE_PAY_PACKAGE_NAME))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent,GOOGLE_PAY_REQUEST_CODE);
        }
        else {
            Toast.makeText(this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context, String packageName)
    {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return  false;
        }
    }

    private static  Uri getUpiPaymentUri(String name, String upiId, String transactionNote,String amount)
    {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tr", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("pa", "INR")
                .build();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(MainActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
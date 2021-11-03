package co.martketing.escanercedulas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//import com.barcode.BarcodeCaptureActivity;
//import com.barcode.InfoTarjeta;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import co.martketing.escanercedulas.models.InfoTarjeta;


public class MainActivity extends AppCompatActivity {
    int RC_BARCODE_CAPTURE = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.init_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callScan();
                callScan();
            }
        });
        findViewById(R.id.btn_showall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowPersons.class));
            }
        });
        findViewById(R.id.btn_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ExportsActivity.class));
            }
        });

    }
    private void activityScan(){
        startActivity(new Intent(this,ScanActivity.class));
    }
private void callScan(){
    Intent intent = new Intent(this, BarcodeCaptureActivity.class);
    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
    startActivityForResult(intent, RC_BARCODE_CAPTURE);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);


                    Log.d("DsiplayValue", "Barcode read: " + barcode.displayValue);
                } else {

                    Log.d("ErrorNull", "No barcode captured, intent data is null");
                }
            } else {
                Log.e("ErrorScan", "Error");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
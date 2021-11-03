package co.martketing.escanercedulas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.wonderkiln.camerakit.CameraConfig;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitController;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.martketing.escanercedulas.helper.MyHelper;
import co.martketing.escanercedulas.interfaces.QRCodeDetectedInterface;
import co.martketing.escanercedulas.models.InfoTarjeta;
import dmax.dialog.SpotsDialog;

public class ScanActivity extends AppCompatActivity implements QRCodeDetectedInterface {
CameraView cameraView;
Button btnDetect;
AlertDialog waitingDialog;
ImageView swFlash;
Context context;
final static String TAG="ScanActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        cameraView =(CameraView)findViewById(R.id.cameraview);
        btnDetect=(Button)findViewById(R.id.btn_detect);
        swFlash=(ImageView)findViewById(R.id.swFlash);
        context=this;
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        waitingDialog = new SpotsDialog
                .Builder()
                .setCancelable(false)
                .setContext(this)
                .setMessage("Por Favor Espere")
                .build();
        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                Log.d(TAG,"Event Camera" + cameraKitEvent.getMessage());
            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap=Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();
                runDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        swFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraView.getFlash()==CameraKit.Constants.FLASH_OFF){
                   cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                   swFlash.setImageResource(R.drawable.flash_on);
                }else{
                    cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                    swFlash.setImageResource(R.drawable.flash_off);
                }
            }
        });
    }

    private void runDetector(Bitmap bitmap) {
        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options=
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_PDF417,
                        FirebaseVisionBarcode.FORMAT_QR_CODE

                ).build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {

                processResult(firebaseVisionBarcodes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Error: " + e.getMessage());
            }
        });
    }
    private void parseResponse(String data){
        InfoTarjeta infoTarjeta = null;
        if(data.length()<150) Log.d(TAG,"Error de tamaño");
        infoTarjeta= new InfoTarjeta();
        String primerApellido="", segundoApellido="", primerNombre="", segundoNombre="",cedula="",rh="" ,fechaNacimiento="",sexo="";
        String alphaAndDigits = data.replaceAll("[^\\p{Alpha}\\p{Digit}\\+\\_]+", " ");
        String[] splitStr = alphaAndDigits.split("\\s+");


        if(!alphaAndDigits.contains("PubDSK"))
        {
            int corrimiento=0;


            Pattern pat = Pattern.compile("[A-Z]");
            Matcher match = pat.matcher(splitStr[2+corrimiento]);
            int lastCapitalIndex = -1;
            if(match.find())
            {
                lastCapitalIndex = match.start();
                Log.d(TAG, "match.start: " + match.start());
                Log.d(TAG, "match.end: " + match.end());
                Log.d(TAG, "splitStr: " + splitStr[2+corrimiento]);
                Log.d(TAG, "splitStr length: " + splitStr[2+corrimiento].length());
                Log.d(TAG, "lastCapitalIndex: " + lastCapitalIndex);
            }
            cedula=splitStr[2+corrimiento].substring(lastCapitalIndex-10,lastCapitalIndex);
            primerApellido=splitStr[2+corrimiento].substring(lastCapitalIndex);
            segundoApellido=splitStr[3+corrimiento];
            primerNombre=splitStr[4+corrimiento];
            /**
             * Se verifica que contenga segundo nombre
             */
            if (Character.isDigit(splitStr[5+corrimiento].charAt(0))){
                corrimiento--;
            }
            else{
                segundoNombre=splitStr[5+corrimiento];
            }

            sexo= splitStr[6+corrimiento].contains("M") ? "Masculino":"Femenino";
            rh= splitStr[6+corrimiento].substring(splitStr[6+corrimiento].length() - 2);
            fechaNacimiento=splitStr[6+corrimiento].substring(2, 10);

        }
        else{
            int corrimiento=0;
            Pattern pat = Pattern.compile("[A-Z]");
            if (splitStr[2+corrimiento].length()>7){
                corrimiento--;
            }


            Matcher match = pat.matcher(splitStr[3+corrimiento]);
            int lastCapitalIndex = -1;
            if(match.find())
            {
                lastCapitalIndex = match.start();

            }

            cedula=splitStr[3+corrimiento].substring(lastCapitalIndex-10, lastCapitalIndex);
            primerApellido=splitStr[3+corrimiento].substring(lastCapitalIndex);
            segundoApellido=splitStr[4+corrimiento];
            primerNombre=splitStr[5+corrimiento];
            segundoNombre=splitStr[6+corrimiento];
            sexo= splitStr[7+corrimiento].contains("M") ? "Masculino":"Femenino";
            rh= splitStr[7+corrimiento].substring(splitStr[7+corrimiento].length() - 2);
            fechaNacimiento=splitStr[7+corrimiento].substring(2, 10);

        }
        /**
         * Se setea el objeto con los datos
         */
        infoTarjeta.setPrimerNombre(primerNombre);
        infoTarjeta.setSegundoNombre(segundoNombre);
        infoTarjeta.setPrimerApellido(primerApellido);
        infoTarjeta.setSegundoApellido(segundoApellido);
        infoTarjeta.setCedula(cedula);
        infoTarjeta.setSexo(sexo);
        infoTarjeta.setFechaNacimiento(fechaNacimiento);
        infoTarjeta.setRh(rh);

        Log.d(TAG,"respuesta"+infoTarjeta.toString());
        showInfo(infoTarjeta);
        waitingDialog.dismiss();
    }

    private void showInfo(final InfoTarjeta infoTarjeta) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                ScanActivity.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.bottom_sheetdialog,
                        (ViewGroup) this.getWindow().getDecorView().getRootView(),//(LinearLayout) findViewById(R.id.bottom_sheet_container)
                        false
                );

        TextView id=bottomSheetView.findViewById(R.id.bottoms_id);
        TextView names=bottomSheetView.findViewById(R.id.bottoms_names);
        TextView lnames=bottomSheetView.findViewById(R.id.bottoms_lnames);
        TextView nac=bottomSheetView.findViewById(R.id.bottoms_nac);
        TextView genre=bottomSheetView.findViewById(R.id.bottoms_genre);
        TextView rh=bottomSheetView.findViewById(R.id.bottoms_rh);
        final EditText edtTemperature=bottomSheetView.findViewById(R.id.bottoms_temp);
        final EditText edtCity=bottomSheetView.findViewById(R.id.bottoms_city);
        final MyHelper myDb= new MyHelper(context);

        bottomSheetView.findViewById(R.id.submit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edtTemperature.getText().toString().trim().isEmpty()&&!edtCity.getText().toString().trim().isEmpty()){
                    waitingDialog.show();
                    Calendar c =Calendar.getInstance();
                    int year=c.get(Calendar.YEAR);
                    int mont=c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);
                    String fecha=year + "-" + mont + "-" + day;
                    Log.d(TAG, "Fecha "+fecha);
                    boolean isInsert=myDb.insertPerson(infoTarjeta,edtTemperature.getText().toString(),fecha,edtCity.getText().toString());
                    if(isInsert){
                        waitingDialog.dismiss();
                        Toast.makeText(context,"Datos insertados",Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                        builder.setMessage("¿Desea insgresar otro documento?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                cameraView.start();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                cameraView.stop();
                                finish();
                            }
                        });
                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{
                        waitingDialog.dismiss();
                        Toast.makeText(context,"Error al insertar datos",Toast.LENGTH_SHORT).show();
                    }
                }else{

                    Toast.makeText(context,"Deben llenarse los campos",Toast.LENGTH_SHORT).show();
                }


            }
        });
        bottomSheetView.findViewById(R.id.bottom_dismis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        id.setText(infoTarjeta.getCedula());
        names.setText(infoTarjeta.getPrimerNombre() + " "+infoTarjeta.getSegundoNombre());
        lnames.setText(infoTarjeta.getPrimerApellido()+" "+ infoTarjeta.getSegundoApellido());
        nac.setText(infoTarjeta.getFechaNacimiento());
        genre.setText(infoTarjeta.getSexo());
        rh.setText(infoTarjeta.getRh());

        bottomSheetDialog.setContentView(bottomSheetView);
        final BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mBehavior.setPeekHeight(bottomSheetView.getHeight());//get the height dynamically
            }
        });
        bottomSheetDialog.show();
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if(firebaseVisionBarcodes.isEmpty()){
            //btnDetect.setText("Re-Escan");
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            Log.d("processResult", "no detect");
            builder.setMessage("No se detecto cedula, Recuerde escanerar codigo de barra o enfocar bien la camara");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    cameraView.start();
                }
            });
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
            waitingDialog.dismiss();

        }else {
            for (FirebaseVisionBarcode item : firebaseVisionBarcodes) {
                int value_type = item.getValueType();
                Log.d("processResult", "Type: " + value_type);

                switch (value_type) {
                    case FirebaseVisionBarcode.TYPE_TEXT:
                        Log.d("processResult", "" + item.getRawValue());
                        parseResponse(item.getRawValue());

                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onQRCodeDetected(Barcode data) {

    }
}

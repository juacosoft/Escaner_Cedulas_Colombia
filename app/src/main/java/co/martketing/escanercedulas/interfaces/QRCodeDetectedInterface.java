package co.martketing.escanercedulas.interfaces;

import com.google.android.gms.vision.barcode.Barcode;

public interface QRCodeDetectedInterface {
    void onQRCodeDetected(Barcode data);
}

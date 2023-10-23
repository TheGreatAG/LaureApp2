package it.uniba.dib.sms2223.laureapp.utils;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
public class QRCodeGenerator {

    public static Bitmap generateQRCode(String data, int width, int height) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            // Crea un oggetto BitMatrix dal testo di input
            BitMatrix bitMatrix = multiFormatWriter.encode(data,
                    BarcodeFormat.QR_CODE, width, height);
            // Converte BitMatrix in Bitmap
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j,
                            bitMatrix.get(i, j) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

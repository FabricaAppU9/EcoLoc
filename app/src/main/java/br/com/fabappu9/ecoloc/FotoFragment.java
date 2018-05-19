package br.com.fabappu9.ecoloc;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by julio on 20/03/18.
 */

@SuppressLint("ValidFragment")
public class FotoFragment extends Fragment {
    public static final String TAG_FOTO_FRAGMENT = "FotoFragment";
    private String TAG = "CadastroActivity";
    private Bitmap foto;
    private int tamFotoWidth, tamFotoHeight;
    private boolean fotoAdd = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    public FotoFragment(int tamFotoWidth, int tamFotoHeight) {
        this.tamFotoWidth = tamFotoWidth;
        this.tamFotoHeight = tamFotoHeight;
    }

    private Bitmap resizeImage(Bitmap foto, int width, int height) {
        int w = foto.getWidth();
        int h = foto.getHeight();

        float scaleX = (float) width / foto.getWidth();
        float scaleY = (float) height / foto.getHeight();

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        return Bitmap.createBitmap(foto, 0, 0, w, h, matrix, false);
    }

    private Bitmap resizeImage(String imag, int targetW, int targetH) {
        // Get the dimensions of the bitPhoto
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imag, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inScaled = false;

        return BitmapFactory.decodeFile(imag, bmOptions);
    }


// ========================= base 64 encode ========================================
    private String Base64EnCode(Bitmap imagBtnPhoto){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagBtnPhoto.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();

        // Log.d(TAG,"!! Base64EnCode !! \n "+encodeImage);

        return Base64.encodeToString(b,Base64.DEFAULT);

    }
// ============================ base 64 decode =========================================
    private Bitmap Base64DeCode(String base64dStr, Context context){
        Toast.makeText(context,"do Base64DeCode ",Toast.LENGTH_SHORT).show();
        byte[] decodedBytes = Base64.decode(base64dStr,Base64.DEFAULT);
        Log.d(TAG,"!! Base64DeCode !!  "+decodedBytes.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
        Toast.makeText(context," Base64DeCode DONE",Toast.LENGTH_SHORT).show();
        return bitmap;
    }


    public void cleanFoto(Resources resources){
        foto = BitmapFactory.decodeResource(resources, R.drawable.ic_user);
        fotoAdd = false;
    }

    // sets
    public void setFoto(String img){
        this.foto = resizeImage(img,tamFotoWidth,tamFotoHeight);
        fotoAdd = true;
        //this.miniatura = resizeImage(foto,tamMiniaturaWidth, tamMiniaturaHeight);
    }

    public void setTamFotoWidth(int tamFotoWidth) {
        this.tamFotoWidth = tamFotoWidth;
    }

    public void setTamFotoHeight(int tamFotoHeight) {
        this.tamFotoHeight = tamFotoHeight;
    }


    // gets
    public Bitmap getFoto(Resources resources) {
        return (foto==null)? BitmapFactory.decodeResource(resources, R.drawable.ic_user):foto;
    }

    public int getTamFotoWidth() {
        return tamFotoWidth;
    }

    public int getTamFotoHeight() {
        return tamFotoHeight;
    }


    public String getFotoBase64(){
        return Base64EnCode(foto);
    }

    public boolean isFotoAdd() {
        return fotoAdd;
    }
}

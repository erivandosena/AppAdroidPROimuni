package br.com.erivando.vacinaskids.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class Uteis {

    /**
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * Habilita modo tela cheia
     *
     * @param context Context o contexto da activity
     */
    public static void habilitaImmersiveMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     *
     * @param context
     */
    public static void habilitaTelaCheia(Context context) {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     *
     * @param nomeCompleto
     * @return
     */
    public static String capitalizeNome(final String nomeCompleto) {
        String[] palavras = nomeCompleto.split(" ");
        StringBuilder sb = new StringBuilder();
        List<String> excessoes = new ArrayList<String>(Arrays.asList("de", "da", "das", "do", "dos", "na", "nas", "no", "nos", "a", "e", "o", "em", "com"));
        for (String palavra : palavras) {
            if (excessoes.contains(palavra.toLowerCase()))
                sb.append(palavra.toLowerCase()).append(" ");
            else
                sb.append(Character.toUpperCase(palavra.charAt(0))).append(palavra.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public static String bitmapParaBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Converte de String Base64 para Bitmap
     *
     * @param b64 String Base64
     * @return Bitmap Imagem Batimap
     */
    public static Bitmap base64ParaBitmap(String b64) {
        Bitmap bitmap;
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return bitmap;
    }

    /**
     *
     * @param arquivo
     * @return
     */
    public static File criaArquivoImagem(File arquivo) {
        File arqImagem = getApplicationContext().getCacheDir();
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        try {
            if (!arquivo.exists()) {
                if (!arquivo.mkdirs()) {
                    arquivo = arqImagem;
                }
            }
            arqImagem = File.createTempFile(
                    imageFileName,  /* prefixo */
                    ".jpg",         /* sufixo */
                    arquivo         /* diretorio */
            );
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return arqImagem;
    }

    /**
     * Converte o BitmapDrawable do ImageView para Bitmpap
     *
     * @param imagemView A imagem do ImageView
     * @return Bitmap Imagem bitmap
     */
    public static Bitmap imageViewBitmap(ImageView imagemView) {
        Bitmap bitmap;
        if (imagemView.getDrawable() instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) imagemView.getDrawable()).getBitmap();
        } else {
            Drawable draw = imagemView.getDrawable();
            bitmap = Bitmap.createBitmap(draw.getIntrinsicWidth(), draw.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            draw.draw(canvas);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        }
        return bitmap;
    }

    /**
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri bitmapParaUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Imagem", null);
        return Uri.parse(path);
    }

    /**
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

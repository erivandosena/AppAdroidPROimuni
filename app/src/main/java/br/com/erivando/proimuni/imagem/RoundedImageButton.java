package br.com.erivando.proimuni.imagem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   01 de Agosto de 2018 as 00:25
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class RoundedImageButton extends AppCompatImageButton {

    private static final String TAG = "RoundedImageButton";

    public RoundedImageButton(Context context) {
        super(context);
    }

    public RoundedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        try {
            Drawable drawable = getDrawable();

            if (drawable == null) {
                return;
            }

            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }

            Bitmap bitmap;
            int w = getWidth(), h = getHeight();

            if (w <= 0 || h <= 0) {
                return;
            }

            if (drawable instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable) drawable).getBitmap();
                bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            } else if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);
                c.drawColor(((ColorDrawable) drawable).getColor());

            } else {
                return;
            }

            Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, Math.min(w, h));
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        } catch (Exception e) {
            Log.e(TAG, "Erro onDraw", e);
        }

    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            finalBitmap = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2,
                finalBitmap.getHeight() / 2,
                finalBitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

}

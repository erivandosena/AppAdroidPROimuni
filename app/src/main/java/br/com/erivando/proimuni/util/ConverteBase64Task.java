package br.com.erivando.proimuni.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   01 de Agosto de 2018 as 00:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ConverteBase64Task extends AsyncTask<Void, Bitmap, Bitmap> {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final File file;
    private int CompressionRatio = 60; //Qualquer proporção que desejar compactar entre 0 a 100.
    private boolean shouldrotate = true;
    private ImageCompressiorListner imageCompressiorListner;

    public ConverteBase64Task(File file) {
        this.file = file;
    }

    public void setRotation(boolean isRotate) {
        shouldrotate = isRotate;
    }

    public void setCompressionRatio(int Ratio) {
        CompressionRatio = Ratio;
    }

    public void setImageCompressiorListner(ImageCompressiorListner imageCompressiorListner) {
        this.imageCompressiorListner = imageCompressiorListner;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            Matrix matrix = new Matrix();

            if (shouldrotate) {
                ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
                int orientation = exif1.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                } else {
                    matrix.postRotate(0);
                }
            } else {
                matrix.postRotate(0);
            }
            try {
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), option);

                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                int scale = 1;

                if (file_size < 512) {
                    /* Log.d("Tamanho bom", "Imagem  menor."); */
                } else if (file_size < 1024) {
                    /* Log.d("Tamanho de 1Mb", "Iimagem pesado."); */
                    scale = 2;
                } else if (file_size < 1536) {
                    /* Log.d("Tamanho de 1.5Mb", "Tamanho levimente pesado."); */
                    scale = 2;
                } else if (file_size < 2048) {
                    /* Log.d("Tamanho de 2Mb", "Tamanho muito pesado."); */
                    scale = 4;
                } else {
                    /* Log.d("Tamanho > de 2Mb", "Tamanho extremamente pesado."); */
                    scale = 4;
                }
                /* Log.e("Escala", "Finalidade com escala " + scale); */
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), o2);

                if (pickimg.getWidth() > 1280 || pickimg.getHeight() > 1000) {

                    int width = pickimg.getWidth();
                    int height = pickimg.getHeight();

                    while (width > 1280 || height > 700) {
                        width = (width * 90) / 100;
                        height = (height * 90) / 100;
                    }

                    pickimg = Bitmap.createScaledBitmap(pickimg, width, height, true);
                } else {
                    pickimg = Bitmap.createBitmap(pickimg, 0, 0, pickimg.getWidth(), pickimg.getHeight(), matrix, true); // rotating bitmap
                }

                pickimg.compress(Bitmap.CompressFormat.JPEG, CompressionRatio, baos);
                return pickimg;

            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
                return null;
            }

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            if (imageCompressiorListner != null) {
                imageCompressiorListner.onImageCompressed(result);
            }
        } else {
            if (imageCompressiorListner != null) {
                imageCompressiorListner.onError();
            }
        }
    }

    public interface ImageCompressiorListner {
        void onImageCompressed(Bitmap bitmap);

        void onError();
    }
}
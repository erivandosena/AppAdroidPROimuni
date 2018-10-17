package br.com.erivando.vacinaskids.database.backup;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import br.com.erivando.vacinaskids.database.RealmDataBase;
import io.realm.Realm;
import io.realm.internal.IOException;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   13 de Agosto de 2018 as 10:01
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class RealmBackupRestore {

    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = "vacinaskids.realm";
    private String IMPORT_REALM_FILE_NAME = "default.realm"; // Eventualmente, substitua isso se estiver usando um nome de banco de dados personalizado
    private final static String TAG = RealmBackupRestore.class.getName();
    private Activity activity;
    private Realm realm;

    // Permissões de armazenamento
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public RealmBackupRestore(Activity activity) {
        this.realm = new RealmDataBase(activity.getApplicationContext()).getRealmInstance();
        this.activity = activity;
    }

    public void backup() {
        //Primeiro, verifique se temos permissões de armazenamento
            if (checkStoragePermissions(activity) == 0) {
                File exportRealmFile;
                try {
                    Log.d(TAG, "Realm DB Path = " + this.dbPath());

                    try {
                        EXPORT_REALM_PATH.mkdirs();

                        //criar um arquivo de backup
                        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

                        //se o arquivo de backup já existir, exclua-o
                        exportRealmFile.delete();

                        // copiar o Realm atual para o arquivo de backup
                        realm.writeCopyTo(exportRealmFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String msg = "Arquivo exportado para o local: " + EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
                    Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Log.d(TAG, msg);
                } finally {
                    realm.close();
                }
            } else {
                Toast.makeText(activity.getApplicationContext(), "Necessário conceder permissões.", Toast.LENGTH_LONG).show();
            }
    }

    public void restore() {
        if (checkStoragePermissions(activity) == 0) {
            //Restaura
            String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

            Log.d(TAG, "oldFilePath = " + restoreFilePath);

            copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME);
            Toast.makeText(activity.getApplicationContext(), "Finalizando restauração...", Toast.LENGTH_LONG).show();
            Log.d(TAG, "A restauração de dados foi concluída!");
        } else {
            Toast.makeText(activity.getApplicationContext(), "Necessário conceder permissões.", Toast.LENGTH_LONG).show();
        }
    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(activity.getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int checkStoragePermissions(Activity activity) {
        // Verifique se temos permissão de gravação
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        return permission;
    }

    private String dbPath(){
        return realm.getPath();
    }
}
package br.com.erivando.proimuni.database.backup;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.RealmDataBase;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import io.realm.Realm;
import io.realm.internal.IOException;

import static br.com.erivando.proimuni.util.Uteis.checkStoragePermissions;
import static br.com.erivando.proimuni.util.Uteis.isExternalStorage;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   13 de Agosto de 2018 as 10:01
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class RealmBackupRestore {

    public static String BACKUP_FILE;
    Context context = AppAplicacao.contextApp;
    private File EXPORT_REALM_EXTERNAL_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private File EXPORT_REALM_INTERNAL_PATH = new File(context.getFilesDir() + File.separator + Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = context.getResources().getString(R.string.app_name) + ".realm";
    private String IMPORT_REALM_FILE_NAME = "default.realm"; // Substituir se estiver usando um nome de banco de dados personalizado
    private Realm realm;

    public RealmBackupRestore(Context context) {
        this.realm = new RealmDataBase(AppAplicacao.contextApp).getRealmInstance();
        this.context = context;
    }

    public void backup() {
        if (checkStoragePermissions(context) == 0) {
            File exportRealmFile = null;

            try {
                //criar um arquivo de menu_backup_copia
                if (isExternalStorage()) {
                    EXPORT_REALM_EXTERNAL_PATH.mkdirs();
                    exportRealmFile = new File(EXPORT_REALM_EXTERNAL_PATH, EXPORT_REALM_FILE_NAME);
                } else {
                    EXPORT_REALM_INTERNAL_PATH.mkdirs();
                    exportRealmFile = new File(EXPORT_REALM_INTERNAL_PATH, EXPORT_REALM_FILE_NAME);
                }

                //se o arquivo já existir, exclua-o
                if (exportRealmFile.exists())
                    exportRealmFile.delete();

                // copiar o Realm atual para o arquivo de menu_backup_copia
                realm.beginTransaction();
                realm.writeCopyTo(exportRealmFile);
                realm.commitTransaction();

                BACKUP_FILE = exportRealmFile.getAbsolutePath();
                Toast.makeText(context, "\nCópia dos dados concluída!\nDisponível localmente em Downloads.\n", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                realm.cancelTransaction();
                e.printStackTrace();
            }

            if (!realm.isClosed())
                realm.close();
        } else {
            Toast.makeText(context, "Necessário conceder permissões.", Toast.LENGTH_LONG).show();
        }
    }

    public void restore() {
        if (checkStoragePermissions(context) == 0) {

            String restoreFilePath = null;

            //Restaura
            if (isExternalStorage()) {
                restoreFilePath = EXPORT_REALM_EXTERNAL_PATH + File.separator + EXPORT_REALM_FILE_NAME;
            } else {
                restoreFilePath = EXPORT_REALM_INTERNAL_PATH + File.separator + EXPORT_REALM_FILE_NAME;
            }
            if (new File(restoreFilePath).exists()) {
                copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME);
                Toast.makeText(context, "Finalizando restauração...", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, "Necessário conceder permissões.", Toast.LENGTH_LONG).show();
        }
    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(context.getFilesDir(), outFileName);

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dbPath() {
        return realm.getPath();
    }
}
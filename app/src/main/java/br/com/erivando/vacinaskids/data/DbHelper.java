package br.com.erivando.vacinaskids.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.DatabaseInfo;
import br.com.erivando.vacinaskids.model.Usuario;


/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 16:29
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class DbHelper extends SQLiteOpenHelper {

    //TABELA USUÁRIO
    public static final String USUA_TABLE_NAME = "usuario";
    public static final String USUA_ID = "usua_id";
    public static final String USUA_NOME = "usua_nome";
    public static final String USUA_LOGIN = "usua_login";
    public static final String USUA_SENHA = "usua_senha";
    public static final String USUA_EMAIL = "usua_email";

    @Inject
    public DbHelper(@ApplicationContext Context context,
                    @DatabaseInfo String dbName,
                    @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USUA_TABLE_NAME);
        onCreate(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + USUA_TABLE_NAME + "("
                            + USUA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + USUA_NOME + " VARCHAR(50), "
                            + USUA_LOGIN + " VARCHAR(10), "
                            + USUA_SENHA + " VARCHAR(50), "
                            + USUA_EMAIL + " VARCHAR(50))"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Usuario obtemUsuario(Integer userId) throws Resources.NotFoundException, NullPointerException {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + USUA_TABLE_NAME
                            + " WHERE "
                            + USUA_ID
                            + " = ? ",
                    new String[]{userId + ""});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Usuario usuario = new Usuario();
                usuario.setUsuaId(cursor.getInt(cursor.getColumnIndex(USUA_ID)));
                usuario.setUsuaNome(cursor.getString(cursor.getColumnIndex(USUA_NOME)));
                usuario.setUsuaLogin(cursor.getString(cursor.getColumnIndex(USUA_LOGIN)));
                usuario.setUsuaSenha(cursor.getString(cursor.getColumnIndex(USUA_SENHA)));
                usuario.setUsuaEmail(cursor.getString(cursor.getColumnIndex(USUA_EMAIL)));
                return usuario;
            } else {
                throw new Resources.NotFoundException("Usuario com ID " + userId + " inexistente!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    protected Long InsereUsuario(Usuario user) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USUA_NOME, user.getUsuaNome());
            contentValues.put(USUA_LOGIN, user.getUsuaLogin());
            contentValues.put(USUA_SENHA, user.getUsuaSenha());
            contentValues.put(USUA_EMAIL, user.getUsuaEmail());
            return db.insert(USUA_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}

package br.com.erivando.vacinaskids.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.util.AppConstants;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 11:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = SqliteHelper.class.getSimpleName();

    @Inject
    public SqliteHelper(@ApplicationContext Context context) {
        super(context, AppConstants.DB_NAME, null, AppConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqliteTable.SCRIPT_TABLE_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteTable.TABLE_USUARIO);
        onCreate(db);
    }

    public boolean insere(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(table, null, values);
        if (result == -1) {
            Log.d(TAG, "Falha ao salvar dados!");
            return false;
        } else {
            Log.d(TAG, "Dados salvos com sucesso!");
            return true;
        }
    }

    /**
     * Este método edita os detalhes do usuário e retorna o valor inteiro.
     *
     * @param tabela  este parametro fornece a tabela que deseja atualizar.
     * @param valores parametros dos valores referentes coluna.
     * @param id      para identificar o que deseja editar.
     * @return retorna um valor inteiro.
     **/
    public int atualiza(String tabela, ContentValues valores, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(tabela, valores, SqliteTable.USUA_ID + " = ? ", new String[]{id.toString()});
    }

    /**
     * Este método realiza um select na tabela pelo id do usuário informado.
     *
     * @param id referente ao código do usuário.
     * @return retorna o objeto usuário.
     */
    public Usuario seleciona(Integer id) {
        Usuario user;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                SqliteTable.USUA_ID,
                SqliteTable.USUA_NOME,
                SqliteTable.USUA_LOGIN,
                SqliteTable.USUA_SENHA,
                SqliteTable.USUA_EMAIL,
        };

        String selection = SqliteTable.USUA_ID + " = ? ";
        String[] args = {id.toString()};

        Cursor cursor = db.query(SqliteTable.TABLE_USUARIO, columns, selection, args, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new Usuario();
            user.setUsuaId(cursor.getInt(0));
            user.setUsuaNome(cursor.getString(1));
            user.setUsuaLogin(cursor.getString(2));
            user.setUsuaSenha(cursor.getString(3));
            user.setUsuaEmail(cursor.getString(4));
            return user;
        }
        return null;
    }

    /**
     * Este método verifica se o usuário existe ou não, e retornará um valor booleano.
     *
     * @param login referente o e-mail do usuário.
     * @param senha referente a senha do usuário.
     * @return retorna um valor boleano true ou false.
     **/
    public boolean valida(String login, String senha) {
        String[] columns = {
                SqliteTable.USUA_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = SqliteTable.USUA_LOGIN + " = ? " + " AND " + SqliteTable.USUA_SENHA + " = ? ";
        String[] args = {login, senha};
        Cursor cursor = db.query(SqliteTable.TABLE_USUARIO,
                columns,    /* return */
                selection,  /* cláusula where */
                args,       /* valor para cláusula where */
                null,
                null,
                null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count > 0) {
            Log.d(TAG, "Retorno true");
            return true;
        }
        return false;
    }

    /**
     * Esse método exclui os detalhes do usuário permanentemente e retorna um valor booleano.
     *
     * @param id param email fornece email do usuário que está logado no aplicativo.
     * @return retorna um valor boleano true ou false.
     **/
    public boolean deleta(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SqliteTable.TABLE_USUARIO, SqliteTable.USUA_ID + " = ? ", new String[]{id.toString()}) > 0;
    }

}

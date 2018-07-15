package br.com.erivando.vacinaskids.database;

import android.content.ContentValues;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.di.ApplicationContext;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 11:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class SqliteController {

    private final SqliteHelper sqliteHelper;
    private final Context context;

    @Inject
    public SqliteController(@ApplicationContext Context contextApplication) {
        this.context = contextApplication;
        sqliteHelper = new SqliteHelper(context);
    }

    public boolean adicionaUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(SqliteTable.USUA_NOME, usuario.getUsuaNome());
        values.put(SqliteTable.USUA_LOGIN, usuario.getUsuaLogin());
        values.put(SqliteTable.USUA_SENHA, usuario.getUsuaSenha());
        values.put(SqliteTable.USUA_EMAIL, usuario.getUsuaEmail());
        return sqliteHelper.insere(SqliteTable.TABLE_USUARIO, values);
    }

    public int editaUsuario(Usuario usuario, Integer id) {
        ContentValues values = new ContentValues();
        values.put(SqliteTable.USUA_NOME, usuario.getUsuaNome());
        values.put(SqliteTable.USUA_LOGIN, usuario.getUsuaLogin());
        values.put(SqliteTable.USUA_SENHA, usuario.getUsuaSenha());
        values.put(SqliteTable.USUA_EMAIL, usuario.getUsuaEmail());
        return sqliteHelper.atualiza(SqliteTable.TABLE_USUARIO, values, id);
    }

    public boolean excluiUsuario(Integer id) {
        return sqliteHelper.deleta(id);
    }

    public Usuario obtemDadosUsuario(Integer id) {
        return sqliteHelper.seleciona(id);
    }

    public boolean validaDadosUsuario(String email, String password) {
        return sqliteHelper.valida(email, password);
    }

}
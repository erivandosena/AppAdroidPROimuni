package br.com.erivando.vacinaskids.database;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 11:20
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class SqliteTable {

    public static final String TABLE_USUARIO = "usuario";
    public static final String USUA_ID = " usua_id";
    public static final String USUA_NOME = " usua_nome";
    public static final String USUA_LOGIN = " usua_login";
    public static final String USUA_SENHA = " usua_senha";
    public static final String USUA_EMAIL = " usua_email";

    public static final String SCRIPT_TABLE_USUARIO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USUARIO + "("
            + USUA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USUA_NOME + " VARCHAR(50), "
            + USUA_LOGIN + " VARCHAR(10), "
            + USUA_SENHA + " VARCHAR(50), "
            + USUA_EMAIL + " VARCHAR(50) "
            + ")";
}

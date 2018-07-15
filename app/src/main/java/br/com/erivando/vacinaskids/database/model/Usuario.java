package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 15:40h
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

/**
 * Classe POJO para mapeamento da tabela usuário.
 * POJO representing table usuario.
 */
public class Usuario implements java.io.Serializable {

    static final long serialVerionUID = 640239297043882001L;

    protected Integer usuaId;
    protected String usuaNome;
    protected String usuaLogin;
    protected String usuaSenha;
    protected String usuaEmail;
    /* Correspondente a interface DAO. */
    //public static final Class<UsuarioDAO> DAO_INTERFACE_CLASS = UsuarioDAO.class;

    /**
     * Método default construtor da classe.
     */
    public Usuario() {
    }

    /**
     * Método construtor de sobrecarga da classe.
     *
     * @param usuaId valor da coluna usua_id.
     *                * @param usuaNome valor da coluna usua_nome.
     *                * @param usuaLogin valor da coluna usua_login.
     *                * @param usuaSenha valor da coluna usua_senha.
     *                * @param usuaEmail valor da coluna usua_email.
     */
    public Usuario(Integer usuaId, String usuaNome, String usuaLogin, String usuaSenha, String usuaEmail) {
        setUsuaId(usuaId);
        setUsuaNome(usuaNome);
        setUsuaLogin(usuaLogin);
        setUsuaSenha(usuaSenha);
        setUsuaEmail(usuaEmail);
    }

    /* métodos Gets e Sets para encapsulamentos. */
    public Integer getUsuaId() {
        return this.usuaId;
    }

    public void setUsuaId(Integer usuaId) {
        this.usuaId = usuaId;
    }

    public String getUsuaNome() {
        return this.usuaNome;
    }

    public void setUsuaNome(String usuaNome) {
        this.usuaNome = usuaNome;
    }

    public String getUsuaLogin() {
        return this.usuaLogin;
    }

    public void setUsuaLogin(String usuaLogin) {
        this.usuaLogin = usuaLogin;
    }

    public String getUsuaSenha() {
        return this.usuaSenha;
    }

    public void setUsuaSenha(String usuaSenha) {
        this.usuaSenha = usuaSenha;
    }

    public String getUsuaEmail() {
        return this.usuaEmail;
    }

    public void setUsuaEmail(String usuaEmail) {
        this.usuaEmail = usuaEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Usuario usuario = (Usuario) o;

        if (usuaId != null ? !usuaId.equals(usuario.usuaId) : usuario.usuaId != null) {
            return false;
        }
        if (usuaNome != null ? !usuaNome.equals(usuario.usuaNome) : usuario.usuaNome != null) {
            return false;
        }
        if (usuaLogin != null ? !usuaLogin.equals(usuario.usuaLogin) : usuario.usuaLogin != null) {
            return false;
        }
        if (usuaSenha != null ? !usuaSenha.equals(usuario.usuaSenha) : usuario.usuaSenha != null) {
            return false;
        }
        if (usuaEmail != null ? !usuaEmail.equals(usuario.usuaEmail) : usuario.usuaEmail != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (usuaId != null ? usuaId.hashCode() : 0);
        result = 31 * result + (usuaNome != null ? usuaNome.hashCode() : 0);
        result = 31 * result + (usuaLogin != null ? usuaLogin.hashCode() : 0);
        result = 31 * result + (usuaSenha != null ? usuaSenha.hashCode() : 0);
        result = 31 * result + (usuaEmail != null ? usuaEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "Código: '" + usuaId + '\''
                + ", Nome: '" + usuaNome + '\''
                + ", Login: '" + usuaLogin + '\''
                + ", Senha: '" + usuaSenha + '\''
                + ", E-mail: '" + usuaEmail + '\''
                + '}';
    }

}

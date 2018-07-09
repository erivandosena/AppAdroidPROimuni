package br.com.erivando.vacinaskids.model;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 15:40h
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

/**
 * POJO representing table usuario.
 */
public class Usuario implements java.io.Serializable {

    /**
     * Serial verion UID
     */
    static final long serialVerionUID = 640239297043882001L;

    /**
     * Corresponding DAO interface class.
     */
//    public static final Class<UsuarioDAO> DAO_INTERFACE_CLASS = UsuarioDAO.class;

    /**
     * Property representing column usua_id
     */
    protected Integer usuaId;
    /**
     * Property representing column usua_nome
     */
    protected String usuaNome;
    /**
     * Property representing column usua_login
     */
    protected String usuaLogin;
    /**
     * Property representing column usua_senha
     */
    protected String usuaSenha;
    /**
     * Property representing column usua_email
     */
    protected String usuaEmail;

    /**
     * Default constructor.
     */
    public Usuario() {
    }

    /**
     * All columns constructor.
     * @param usuaId value of column usua_id.
     * @param usuaNome value of column usua_nome.
     * @param usuaLogin value of column usua_login.
     * @param usuaSenha value of column usua_senha.
     * @param usuaEmail value of column usua_email.
     */
    public Usuario(Integer usuaId, String usuaNome, String usuaLogin, String usuaSenha, String usuaEmail) {
        setUsuaId(usuaId);
        setUsuaNome(usuaNome);
        setUsuaLogin(usuaLogin);
        setUsuaSenha(usuaSenha);
        setUsuaEmail(usuaEmail);
    }

    /**
     * Returns value of property {@link #usuaId}.
     * @return value of property {@link #usuaId}.
     */
    public Integer getUsuaId() {
        return this.usuaId;
    }
    /**
     * Sets new value of property {@link #usuaId}.
     * @param usuaId new value of property {@link #usuaId}.
     */
    public void setUsuaId(Integer usuaId) {
        this.usuaId = usuaId;
    }
    /**
     * Returns value of property {@link #usuaNome}.
     * @return value of property {@link #usuaNome}.
     */
    public String getUsuaNome() {
        return this.usuaNome;
    }
    /**
     * Sets new value of property {@link #usuaNome}.
     * @param usuaNome new value of property {@link #usuaNome}.
     */
    public void setUsuaNome(String usuaNome) {
        this.usuaNome = usuaNome;
    }
    /**
     * Returns value of property {@link #usuaLogin}.
     * @return value of property {@link #usuaLogin}.
     */
    public String getUsuaLogin() {
        return this.usuaLogin;
    }
    /**
     * Sets new value of property {@link #usuaLogin}.
     * @param usuaLogin new value of property {@link #usuaLogin}.
     */
    public void setUsuaLogin(String usuaLogin) {
        this.usuaLogin = usuaLogin;
    }
    /**
     * Returns value of property {@link #usuaSenha}.
     * @return value of property {@link #usuaSenha}.
     */
    public String getUsuaSenha() {
        return this.usuaSenha;
    }
    /**
     * Sets new value of property {@link #usuaSenha}.
     * @param usuaSenha new value of property {@link #usuaSenha}.
     */
    public void setUsuaSenha(String usuaSenha) {
        this.usuaSenha = usuaSenha;
    }
    /**
     * Returns value of property {@link #usuaEmail}.
     * @return value of property {@link #usuaEmail}.
     */
    public String getUsuaEmail() {
        return this.usuaEmail;
    }
    /**
     * Sets new value of property {@link #usuaEmail}.
     * @param usuaEmail new value of property {@link #usuaEmail}.
     */
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
                + "ID: '" + usuaId + '\''
                + ", Nome: '" + usuaNome + '\''
                + ", Login: '" + usuaLogin + '\''
                + ", Senha: '" + usuaSenha + '\''
                + ", E-mail: '" + usuaEmail + '\''
                + '}';
    }
}

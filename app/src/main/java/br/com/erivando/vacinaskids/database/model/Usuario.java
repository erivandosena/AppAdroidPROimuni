package br.com.erivando.vacinaskids.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 15:40h
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


/**
 * Classe POJO para mapeamento da tabela usuário.
 * POJO representing table usuário.
 */
public class Usuario extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private String usuaNome;
    private String usuaLogin;
    private String usuaSenha;
    private String usuaEmail;

    /* Correspondente a interface DAO. */
    //public static final Class<UsuarioDAO> DAO_INTERFACE_CLASS = UsuarioDAO.class;

    /**
     * Método default construtor da classe.
     */
    public Usuario() {
    }

    //@Generated(hash = 1015687459)
    public Usuario(Long id, String usuaNome, String usuaLogin, String usuaSenha, String usuaEmail) {
        this.id = id;
        this.usuaNome = usuaNome;
        this.usuaLogin = usuaLogin;
        this.usuaSenha = usuaSenha;
        this.usuaEmail = usuaEmail;
    }

    /* métodos Gets e Sets para encapsulamentos. */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

        if (id != null ? !id.equals(usuario.id) : usuario.id != null) {
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
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (usuaNome != null ? usuaNome.hashCode() : 0);
        result = 31 * result + (usuaLogin != null ? usuaLogin.hashCode() : 0);
        result = 31 * result + (usuaSenha != null ? usuaSenha.hashCode() : 0);
        result = 31 * result + (usuaEmail != null ? usuaEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "Código: '" + id + '\''
                + ", Nome: '" + usuaNome + '\''
                + ", Login: '" + usuaLogin + '\''
                + ", Senha: '" + usuaSenha + '\''
                + ", E-mail: '" + usuaEmail + '\''
                + '}';
    }

}

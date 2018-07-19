package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 15:40h
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Classe POJO para mapeamento da tabela usuário.
 * POJO representing table usuario.
 */

public class Usuario extends RealmObject implements Serializable {


    static final long serialVerionUID = 640239297043882001L;

    //@Id(autoincrement = true)
    //@NotNull
    //@Property(nameInDb = "usua_id")
    @Required
    @PrimaryKey
    //@RealmField(name = "usua_id")
    private Long usuaId;

    //@Property(nameInDb = "usua_nome")
    //@RealmField(name = "usua_nome")
    private String usuaNome;

    //@Property(nameInDb = "usua_login")
    //@RealmField(name = "usua_login")
    private String usuaLogin;

    //@Property(nameInDb = "usua_senha")
    //@RealmField(name = "usua_senha")
    private String usuaSenha;

    //@Property(nameInDb = "usua_email")
    //@RealmField(name = "usua_email")
    private String usuaEmail;
    /* Correspondente a interface DAO. */
    //public static final Class<UsuarioDAO> DAO_INTERFACE_CLASS = UsuarioDAO.class;

    /**
     * Método default construtor da classe.
     */
    public Usuario() {
    }

    //@Generated(hash = 1015687459)
    public Usuario(Long usuaId, String usuaNome, String usuaLogin, String usuaSenha, String usuaEmail) {
        this.usuaId = usuaId;
        this.usuaNome = usuaNome;
        this.usuaLogin = usuaLogin;
        this.usuaSenha = usuaSenha;
        this.usuaEmail = usuaEmail;
    }

    /* métodos Gets e Sets para encapsulamentos. */
    public Long getUsuaId() {
        return this.usuaId;
    }

    public void setUsuaId(Long usuaId) {
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

package br.com.erivando.vacinaskids.database.model;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Usuario extends RealmObject implements Parcelable {

    @Required
    @PrimaryKey
    private Long id;
    private String usuaNome;
    private String usuaLogin;
    private String usuaSenha;
    private String usuaEmail;
    private String usuaFoto;

    public Usuario() {
    }

    public Usuario(Long id, String usuaNome, String usuaLogin, String usuaSenha, String usuaEmail, String usuaFoto) {
        this.id = id;
        this.usuaNome = usuaNome;
        this.usuaLogin = usuaLogin;
        this.usuaSenha = usuaSenha;
        this.usuaEmail = usuaEmail;
        this.usuaFoto = usuaFoto;
    }

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

    public String getUsuaFoto() {
        return usuaFoto;
    }

    public void setUsuaFoto(String usuaFoto) {
        this.usuaFoto = usuaFoto;
    }

    @Override
    public String toString() {
        return "{"
                + "Código: '" + id + '\''
                + ", Nome: '" + usuaNome + '\''
                + ", Login: '" + usuaLogin + '\''
                + ", Senha: '" + usuaSenha + '\''
                + ", E-mail: '" + usuaEmail + '\''
                + ", Foto: '" + usuaFoto + '\''
                + '}';
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    protected Usuario(Parcel in) {
        id = in.readLong();
        usuaNome = in.readString();
        usuaLogin = in.readString();
        usuaSenha = in.readString();
        usuaEmail = in.readString();
        usuaFoto = in.readString();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(usuaNome);
        dest.writeString(usuaLogin);
        dest.writeString(usuaSenha);
        dest.writeString(usuaEmail);
        dest.writeString(usuaFoto);
    }
}

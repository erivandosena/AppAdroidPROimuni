package br.com.erivando.proimuni.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:33
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table crianca.
 */
public class Crianca extends RealmObject implements Parcelable {

    public static final Parcelable.Creator<Crianca> CREATOR = new Parcelable.Creator<Crianca>() {
        @Override
        public Crianca createFromParcel(Parcel in) {
            return new Crianca(in);
        }

        @Override
        public Crianca[] newArray(int size) {
            return new Crianca[size];
        }
    };
    @Required
    @PrimaryKey
    private Long id;
    private Usuario usuario;
    private String criaNome;
    private Date criaNascimento;
    private String criaSexo;
    private String criaFoto;
    private boolean criaEtnia;

    public Crianca() {
    }

    public Crianca(Long id, Usuario usuario, String criaNome, Date criaNascimento, String criaSexo, String criaFoto, boolean criaEtnia) {
        this.id = id;
        this.usuario = usuario;
        this.criaNome = criaNome;
        this.criaNascimento = criaNascimento;
        this.criaSexo = criaSexo;
        this.criaFoto = criaFoto;
        this.criaEtnia = criaEtnia;
    }

    protected Crianca(Parcel in) {
        Long data = in.readLong();

        id = in.readLong();
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        criaNome = in.readString();
        criaNascimento = data == -1 ? null : new Date(data);
        criaSexo = in.readString();
        criaFoto = in.readString();
        criaEtnia = in.readInt() == 1 ? true : false ;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCriaNome() {
        return this.criaNome;
    }

    public void setCriaNome(String criaNome) {
        this.criaNome = criaNome;
    }

    public Date getCriaNascimento() {
        return this.criaNascimento;
    }

    public void setCriaNascimento(Date criaNascimento) {
        this.criaNascimento = criaNascimento;
    }

    public String getCriaSexo() {
        return this.criaSexo;
    }

    public void setCriaSexo(String criaSexo) {
        this.criaSexo = criaSexo;
    }

    public String getCriaFoto() {
        return criaFoto;
    }

    public void setCriaFoto(String criaFoto) {
        this.criaFoto = criaFoto;
    }

    public boolean isCriaEtnia() {
        return criaEtnia;
    }

    public void setCriaEtnia(boolean criaEtnia) {
        this.criaEtnia = criaEtnia;
    }

    @Override
    public String toString() {
        return getCriaNome();
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
        dest.writeParcelable(usuario, flags);
        dest.writeString(criaNome);
        dest.writeLong(criaNascimento.getTime());
        dest.writeString(criaSexo);
        dest.writeString(criaFoto);
        dest.writeInt(criaEtnia == true ? 1 : 0);
    }
}
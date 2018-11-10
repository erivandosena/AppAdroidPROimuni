package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:38
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table cartao.
 */
public class Cartao extends RealmObject implements Parcelable {

    public static final Creator<Cartao> CREATOR = new Creator<Cartao>() {
        @Override
        public Cartao createFromParcel(Parcel in) {
            return new Cartao(in);
        }

        @Override
        public Cartao[] newArray(int size) {
            return new Cartao[size];
        }
    };
    @Required
    @PrimaryKey
    private Long id;
    private Crianca crianca;
    private RealmList<Imunizacao> imunizacoes;

    public Cartao() {
    }

    public Cartao(Long id, Crianca crianca) {
        this.id = id;
        this.crianca = crianca;
    }

    public Cartao(Long id, Crianca crianca, RealmList<Imunizacao> imunizacoes) {
        this.id = id;
        this.crianca = crianca;
        this.imunizacoes = imunizacoes;
    }

    protected Cartao(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        crianca = in.readParcelable(Crianca.class.getClassLoader());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crianca getCrianca() {
        return crianca;
    }

    public void setCrianca(Crianca crianca) {
        this.crianca = crianca;
    }

    public RealmList<Imunizacao> getImunizacoes() {
        return imunizacoes;
    }

    public void setImunizacoes(RealmList<Imunizacao> imunizacoes) {
        this.imunizacoes = imunizacoes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeParcelable(crianca, flags);
    }
}

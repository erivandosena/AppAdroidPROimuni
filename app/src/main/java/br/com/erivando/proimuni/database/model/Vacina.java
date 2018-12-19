package br.com.erivando.proimuni.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table vacina.
 */
public class Vacina extends RealmObject implements Comparable<Vacina> {

    @Required
    @PrimaryKey
    private Long id;
    private String vaciNome;
    private String vaciRede;
    private String vaciDescricao;
    private String vaciAdministracao;

    public Vacina() {
    }

    public Vacina(Long id, String vaciNome, String vaciRede, String vaciDescricao, String vaciAdministracao) {
        this.id = id;
        this.vaciNome = vaciNome;
        this.vaciRede = vaciRede;
        this.vaciDescricao = vaciDescricao;
        this.vaciAdministracao = vaciAdministracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVaciNome() {
        return vaciNome;
    }

    public void setVaciNome(String vaciNome) {
        this.vaciNome = vaciNome;
    }

    public String getVaciRede() {
        return vaciRede;
    }

    public void setVaciRede(String vaciRede) {
        this.vaciRede = vaciRede;
    }

    public String getVaciDescricao() {
        return vaciDescricao;
    }

    public void setVaciDescricao(String vaciDescricao) {
        this.vaciDescricao = vaciDescricao;
    }

    public String getVaciAdministracao() {
        return vaciAdministracao;
    }

    public void setVaciAdministracao(String vaciAdministracao) {
        this.vaciAdministracao = vaciAdministracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacina)) return false;

        Vacina vacina = (Vacina) o;

        if (!getId().equals(vacina.getId())) return false;
        if (!getVaciNome().equals(vacina.getVaciNome())) return false;
        if (!getVaciRede().equals(vacina.getVaciRede())) return false;
        if (!getVaciDescricao().equals(vacina.getVaciDescricao())) return false;
        return getVaciAdministracao().equals(vacina.getVaciAdministracao());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getVaciNome().hashCode();
        result = 31 * result + getVaciRede().hashCode();
        result = 31 * result + getVaciDescricao().hashCode();
        result = 31 * result + getVaciAdministracao().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getVaciNome();
    }


    @Override
    public int compareTo(@NonNull Vacina o) {
        return o.id.intValue() - this.id.intValue();
    }
}
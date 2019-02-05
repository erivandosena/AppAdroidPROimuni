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
    private String vaciDoses;
    private String vaciFaixa;
    private String vaciContraindicacoes;
    private String vaciFonte;

    public Vacina() {
    }

    public Vacina(Long id, String vaciNome, String vaciRede, String vaciDescricao, String vaciAdministracao, String vaciDoses, String vaciFaixa, String vaciContraindicacoes, String vaciFonte) {
        this.id = id;
        this.vaciNome = vaciNome;
        this.vaciRede = vaciRede;
        this.vaciDescricao = vaciDescricao;
        this.vaciAdministracao = vaciAdministracao;
        this.vaciDoses = vaciDoses;
        this.vaciFaixa = vaciFaixa;
        this.vaciContraindicacoes = vaciContraindicacoes;
        this.vaciFonte = vaciFonte;
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

    public String getVaciDoses() {
        return vaciDoses;
    }

    public void setVaciDoses(String vaciDoses) {
        this.vaciDoses = vaciDoses;
    }

    public String getVaciFaixa() {
        return vaciFaixa;
    }

    public void setVaciFaixa(String vaciFaixa) {
        this.vaciFaixa = vaciFaixa;
    }

    public String getVaciContraindicacoes() {
        return vaciContraindicacoes;
    }

    public void setVaciContraindicacoes(String vaciContraindicacoes) {
        this.vaciContraindicacoes = vaciContraindicacoes;
    }

    public String getVaciFonte() {
        return vaciFonte;
    }

    public void setVaciFonte(String vaciFonte) {
        this.vaciFonte = vaciFonte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacina)) return false;

        Vacina vacina = (Vacina) o;

        if (getId() != null ? !getId().equals(vacina.getId()) : vacina.getId() != null)
            return false;
        if (getVaciNome() != null ? !getVaciNome().equals(vacina.getVaciNome()) : vacina.getVaciNome() != null)
            return false;
        if (getVaciRede() != null ? !getVaciRede().equals(vacina.getVaciRede()) : vacina.getVaciRede() != null)
            return false;
        if (getVaciDescricao() != null ? !getVaciDescricao().equals(vacina.getVaciDescricao()) : vacina.getVaciDescricao() != null)
            return false;
        if (getVaciAdministracao() != null ? !getVaciAdministracao().equals(vacina.getVaciAdministracao()) : vacina.getVaciAdministracao() != null)
            return false;
        if (getVaciDoses() != null ? !getVaciDoses().equals(vacina.getVaciDoses()) : vacina.getVaciDoses() != null)
            return false;
        if (getVaciFaixa() != null ? !getVaciFaixa().equals(vacina.getVaciFaixa()) : vacina.getVaciFaixa() != null)
            return false;
        if (getVaciContraindicacoes() != null ? !getVaciContraindicacoes().equals(vacina.getVaciContraindicacoes()) : vacina.getVaciContraindicacoes() != null)
            return false;
        return getVaciFonte() != null ? getVaciFonte().equals(vacina.getVaciFonte()) : vacina.getVaciFonte() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getVaciNome() != null ? getVaciNome().hashCode() : 0);
        result = 31 * result + (getVaciRede() != null ? getVaciRede().hashCode() : 0);
        result = 31 * result + (getVaciDescricao() != null ? getVaciDescricao().hashCode() : 0);
        result = 31 * result + (getVaciAdministracao() != null ? getVaciAdministracao().hashCode() : 0);
        result = 31 * result + (getVaciDoses() != null ? getVaciDoses().hashCode() : 0);
        result = 31 * result + (getVaciFaixa() != null ? getVaciFaixa().hashCode() : 0);
        result = 31 * result + (getVaciContraindicacoes() != null ? getVaciContraindicacoes().hashCode() : 0);
        result = 31 * result + (getVaciFonte() != null ? getVaciFonte().hashCode() : 0);
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

package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:38
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table cartao.
 */
public class Cartao extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private Crianca crianca;
    private Vacina vacina;
    private Classificacao classificacao;

    /**
     * Default constructor.
     */
    public Cartao() {

    }

    /**
     * All columns constructor.
     * @param id value of column cart_id.
     * @param crianca value of column cria_id.
     * @param vacina value of column vaci_id.
     * @param classificacao value of column clas_id.
     */
    public Cartao(Long id, Crianca crianca, Vacina vacina, Classificacao classificacao) {
        setId(id);
        setCrianca(crianca);
        setVacina(vacina);
        setClassificacao(classificacao);
    }

    /**
     * Returns value of property {@link #id}.
     * @return value of property {@link #id}.
     */
    public Long getId() {
        return this.id;
    }
    /**
     * Sets new value of property {@link #id}.
     * @param id new value of property {@link #id}.
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Crianca getCrianca() {
        return crianca;
    }

    public void setCrianca(Crianca crianca) {
        this.crianca = crianca;
    }

    /**
     * Returns value of property {@link #vacina}.
     * @return value of property {@link #vacina}.
     */
    public Vacina getVacina() {
        return this.vacina;
    }
    /**
     * Sets new value of property {@link #vacina}.
     * @param vacina new value of property {@link #vacina}.
     */
    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }
    /**
     * Returns value of property {@link #classificacao}.
     * @return value of property {@link #classificacao}.
     */
    public Classificacao getClassificacao() {
        return this.classificacao;
    }
    /**
     * Sets new value of property {@link #classificacao}.
     * @param classificacao new value of property {@link #classificacao}.
     */
    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cartao cartao = (Cartao) o;
        return Objects.equals(id, cartao.id) &&
                Objects.equals(crianca, cartao.crianca) &&
                Objects.equals(vacina, cartao.vacina) &&
                Objects.equals(classificacao, cartao.classificacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, crianca, vacina, classificacao);
    }

    @Override
    public String toString() {
        return "Cartao{"
                + "id='" + id + '\''
                + ", crianca='" + crianca + '\''
                + ", vacina='" + vacina + '\''
                + ", classificacao='" + classificacao + '\''
                + '}';
    }
}

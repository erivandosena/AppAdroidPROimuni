package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:42
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table classificacao.
 */
public class Classificacao extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private Long clasRanking;

    /**
     * Default constructor.
     */
    public Classificacao() {
    }

    /**
     * All columns constructor.
     * @param id value of column clas_id.
     * @param clasRanking value of column clas_ranking.
     */
    public Classificacao(Long id, Long clasRanking) {
        setId(id);
        setClasRanking(clasRanking);
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
    /**
     * Returns value of property {@link #clasRanking}.
     * @return value of property {@link #clasRanking}.
     */
    public Long getClasRanking() {
        return this.clasRanking;
    }
    /**
     * Sets new value of property {@link #clasRanking}.
     * @param clasRanking new value of property {@link #clasRanking}.
     */
    public void setClasRanking(Long clasRanking) {
        this.clasRanking = clasRanking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classificacao classificacao = (Classificacao) o;

        if (id != null ? !id.equals(classificacao.id) : classificacao.id != null) {
            return false;
        }
        if (clasRanking != null ? !clasRanking.equals(classificacao.clasRanking) : classificacao.clasRanking != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (clasRanking != null ? clasRanking.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Classificacao{"
                + "id='" + id + '\''
                + ", clasRanking='" + clasRanking + '\''
                + '}';
    }
}

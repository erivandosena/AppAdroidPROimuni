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

    public Classificacao() {
    }

    public Classificacao(Long id, Long clasRanking) {
        setId(id);
        setClasRanking(clasRanking);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClasRanking() {
        return this.clasRanking;
    }

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
        return clasRanking != null ? clasRanking.equals(classificacao.clasRanking) : classificacao.clasRanking == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (clasRanking != null ? clasRanking.hashCode() : 0);
        return result;
    }

}

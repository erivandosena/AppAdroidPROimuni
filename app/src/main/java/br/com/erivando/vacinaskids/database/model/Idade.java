package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:48
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Idade extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private String idadDescricao;

    public Idade() {
    }

    public Idade(Long id, String idadDescricao) {
        this.id = id;
        this.idadDescricao = idadDescricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdadDescricao() {
        return idadDescricao;
    }

    public void setIdadDescricao(String idadDescricao) {
        this.idadDescricao = idadDescricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Idade)) return false;

        Idade idade = (Idade) o;

        if (!getId().equals(idade.getId())) return false;
        return getIdadDescricao().equals(idade.getIdadDescricao());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getIdadDescricao().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getIdadDescricao();
    }
}

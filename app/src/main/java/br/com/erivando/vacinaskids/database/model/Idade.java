package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:48
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import java.util.Objects;

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
    public String toString() {
        return getIdadDescricao();
    }
}

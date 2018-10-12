package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:43
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table dose.
 */
public class Dose extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private String doseDescricao;

    public Dose() {
    }

    public Dose(Long id, String doseDescricao) {
        this.id = id;
        this.doseDescricao = doseDescricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoseDescricao() {
        return doseDescricao;
    }

    public void setDoseDescricao(String doseDescricao) {
        this.doseDescricao = doseDescricao;
    }

    @Override
    public String toString() {
        return getDoseDescricao();
    }
}

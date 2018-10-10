package br.com.erivando.vacinaskids.database.model;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   06 de Agosto de 2018 as 23:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * POJO representing table vacina.
 */
public class Vacina extends RealmObject {

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
    public String toString() {
        return "'vacina': [{" +
                "id:" + id +
                ", 'vaciNome':'" + vaciNome + '\'' +
                ", 'vaciRede':'" + vaciRede + '\'' +
                ", 'vaciDescricao':'" + vaciDescricao + '\'' +
                ", 'vaciAdministracao':'" + vaciAdministracao + '\'' +
                "}]";
    }

}

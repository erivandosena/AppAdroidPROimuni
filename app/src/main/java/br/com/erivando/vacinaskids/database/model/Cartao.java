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

    public Cartao() {

    }

    public Cartao(Long id, Crianca crianca, Vacina vacina, Classificacao classificacao) {
        setId(id);
        setCrianca(crianca);
        setVacina(vacina);
        setClassificacao(classificacao);
    }

    public Long getId() {
        return this.id;
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

    public Vacina getVacina() {
        return this.vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public Classificacao getClassificacao() {
        return this.classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public String toString() {
        return "{"
                + "Código: '" + id + '\''
                + ", crianca: '" + crianca.toString() + '\''
                + ", vacina: '" + vacina.toString() + '\''
                + ", classificacao: '" + classificacao.toString() + '\''
                + '}';
    }
}

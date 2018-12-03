package br.com.erivando.proimuni.database.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Outubro de 2018 as 08:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class Imunizacao extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private Date imunData;
    private String imunLote;
    private String imunAgente;
    private String imunPosto;
    private Vacina vacina;
    private Dose dose;
    private Idade idade;
    private Cartao cartao;

    public Imunizacao() {

    }

    public Imunizacao(Long id, Date imunData, String imunLote, String imunAgente, String imunPosto, Cartao cartao) {
        this.id = id;
        this.imunData = imunData;
        this.imunLote = imunLote;
        this.imunAgente = imunAgente;
        this.imunPosto = imunPosto;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getImunData() {
        return imunData;
    }

    public void setImunData(Date imunData) {
        this.imunData = imunData;
    }

    public String getImunLote() {
        return imunLote;
    }

    public void setImunLote(String imunLote) {
        this.imunLote = imunLote;
    }

    public String getImunAgente() {
        return imunAgente;
    }

    public void setImunAgente(String imunAgente) {
        this.imunAgente = imunAgente;
    }

    public String getImunPosto() {
        return imunPosto;
    }

    public void setImunPosto(String imunPosto) {
        this.imunPosto = imunPosto;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public Dose getDose() {
        return dose;
    }

    public void setDose(Dose dose) {
        this.dose = dose;
    }

    public Idade getIdade() {
        return idade;
    }

    public void setIdade(Idade idade) {
        this.idade = idade;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    @Override
    public String toString() {
        return "["+getVacina().getVaciNome()+","+getDose().getDoseDescricao()+","+getCartao().getId()+"]";
    }
}

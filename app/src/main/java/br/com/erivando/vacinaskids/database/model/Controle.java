package br.com.erivando.vacinaskids.database.model;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 00:44
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class Controle extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private Idade idade;
    private Vacina vacina;
    private Dose dose;

    public Controle() {
    }

    public Controle(Long id, Idade idade, Vacina vacina) {
        this.id = id;
        this.idade = idade;
        this.vacina = vacina;
        //this.dose = dose;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Idade getIdade() {
        return idade;
    }

    public void setIdade(Idade idade) {
        this.idade = idade;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controle controle = (Controle) o;
        return Objects.equals(id, controle.id) &&
                Objects.equals(idade, controle.idade) &&
                Objects.equals(vacina, controle.vacina) &&
                Objects.equals(dose, controle.dose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idade, vacina, dose);
    }

    @Override
    public String toString() {
        return "{"
                + "Código: '" + id + '\''
                + ", idade: '" + idade.toString() + '\''
                + ", vacina: '" + vacina.toString() + '\''
                //+ ", dose: '" + dose.toString() + '\''
                + '}';
    }
}

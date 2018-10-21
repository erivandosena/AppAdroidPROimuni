package br.com.erivando.vacinaskids.database.model;

import android.support.annotation.NonNull;

import io.realm.RealmList;
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

public class Calendario extends RealmObject implements Comparable<Calendario> {

    @Required
    @PrimaryKey
    private Long id;
    private Idade idade;
    private Vacina vacina;
    private Dose dose;
    private String tituloIdade;
    private RealmList<Vacina> vacinasInSection;
    private RealmList<Dose> dosesInSection;
    private RealmList<Idade> idadesInSection;

    public Calendario() {
    }

    public Calendario(Long id, Idade idade, Vacina vacina, Dose dose, String tituloIdade, RealmList<Vacina> vacinasInSection, RealmList<Dose> dosesInSection, RealmList<Idade> idadesInSection) {
        this.id = id;
        this.idade = idade;
        this.vacina = vacina;
        this.dose = dose;
        this.tituloIdade = tituloIdade;
        this.vacinasInSection = vacinasInSection;
        this.dosesInSection = dosesInSection;
        this.idadesInSection = idadesInSection;
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

    public String getTituloIdade() {
        return tituloIdade;
    }

    public void setTituloIdade(String tituloIdade) {
        this.tituloIdade = tituloIdade;
    }

    public RealmList<Vacina> getVacinasInSection() {
        return vacinasInSection;
    }

    public void setVacinasInSection(RealmList<Vacina> vacinasInSection) {
        this.vacinasInSection = vacinasInSection;
    }

    public RealmList<Dose> getDosesInSection() {
        return dosesInSection;
    }

    public void setDosesInSection(RealmList<Dose> dosesInSection) {
        this.dosesInSection = dosesInSection;
    }

    public RealmList<Idade> getIdadesInSection() {
        return idadesInSection;
    }

    public void setIdadesInSection(RealmList<Idade> idadesInSection) {
        this.idadesInSection = idadesInSection;
    }

    @Override
    public int compareTo(@NonNull Calendario o) {
        return o.id.intValue() - this.id.intValue();
    }
}

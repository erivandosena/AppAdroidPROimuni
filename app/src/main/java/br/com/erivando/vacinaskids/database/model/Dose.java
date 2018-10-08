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
    private Date doseData;
    private String doseLote;
    private String doseUnidade;
    private String doseAgente;

    /**
     * Default constructor.
     */
    public Dose() {
    }

    /**
     * All columns constructor.
     * @param id value of column dose_id.
     * @param doseData value of column dose_data.
     * @param doseLote value of column dose_lote.
     * @param doseUnidade value of column dose_unidade.
     * @param doseAgente value of column dose_agente.
     */
    public Dose(Long id, java.sql.Date doseData, String doseLote, String doseUnidade, String doseAgente) {
        setId(id);
        setDoseData(doseData);
        setDoseLote(doseLote);
        setDoseUnidade(doseUnidade);
        setDoseAgente(doseAgente);
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
     * Returns value of property {@link #doseData}.
     * @return value of property {@link #doseData}.
     */
    public Date getDoseData() {
        return this.doseData;
    }
    /**
     * Sets new value of property {@link #doseData}.
     * @param doseData new value of property {@link #doseData}.
     */
    public void setDoseData(Date doseData) {
        this.doseData = doseData;
    }
    /**
     * Returns value of property {@link #doseLote}.
     * @return value of property {@link #doseLote}.
     */
    public String getDoseLote() {
        return this.doseLote;
    }
    /**
     * Sets new value of property {@link #doseLote}.
     * @param doseLote new value of property {@link #doseLote}.
     */
    public void setDoseLote(String doseLote) {
        this.doseLote = doseLote;
    }
    /**
     * Returns value of property {@link #doseUnidade}.
     * @return value of property {@link #doseUnidade}.
     */
    public String getDoseUnidade() {
        return this.doseUnidade;
    }
    /**
     * Sets new value of property {@link #doseUnidade}.
     * @param doseUnidade new value of property {@link #doseUnidade}.
     */
    public void setDoseUnidade(String doseUnidade) {
        this.doseUnidade = doseUnidade;
    }
    /**
     * Returns value of property {@link #doseAgente}.
     * @return value of property {@link #doseAgente}.
     */
    public String getDoseAgente() {
        return this.doseAgente;
    }
    /**
     * Sets new value of property {@link #doseAgente}.
     * @param doseAgente new value of property {@link #doseAgente}.
     */
    public void setDoseAgente(String doseAgente) {
        this.doseAgente = doseAgente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dose dose = (Dose) o;

        if (id != null ? !id.equals(dose.id) : dose.id != null) {
            return false;
        }
        if (doseData != null ? !doseData.equals(dose.doseData) : dose.doseData != null) {
            return false;
        }
        if (doseLote != null ? !doseLote.equals(dose.doseLote) : dose.doseLote != null) {
            return false;
        }
        if (doseUnidade != null ? !doseUnidade.equals(dose.doseUnidade) : dose.doseUnidade != null) {
            return false;
        }
        if (doseAgente != null ? !doseAgente.equals(dose.doseAgente) : dose.doseAgente != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (doseData != null ? doseData.hashCode() : 0);
        result = 31 * result + (doseLote != null ? doseLote.hashCode() : 0);
        result = 31 * result + (doseUnidade != null ? doseUnidade.hashCode() : 0);
        result = 31 * result + (doseAgente != null ? doseAgente.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{"
                + "Código: '" + id + '\''
                + ", doseData: '" + doseData + '\''
                + ", doseLote: '" + doseLote + '\''
                + ", doseUnidade: '" + doseUnidade + '\''
                + ", doseAgente: '" + doseAgente + '\''
                + '}';
    }
}

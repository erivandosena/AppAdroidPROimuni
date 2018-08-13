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

/**
 * POJO representing table idade.
 */
public class Idade extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    private Vacina vacina;
    private String idadNascer;
    private String idad2m;
    private String idad3m;
    private String idad4m;
    private String idad5m;
    private String idad6m;
    private String idad9m;
    private String idad12m;
    private String idad15m;
    private String idad4a;
    private String idad11a14a;

    /**
     * Default constructor.
     */
    public Idade() {
    }

    /**
     * All columns constructor.
     * @param id value of column idad_id.
     * @param vacina value of column vaci_id.
     * @param idadNascer value of column idad_nascer.
     * @param idad2m value of column idad_2m.
     * @param idad3m value of column idad_3m.
     * @param idad4m value of column idad_4m.
     * @param idad5m value of column idad_5m.
     * @param idad6m value of column idad_6m.
     * @param idad9m value of column idad_9m.
     * @param idad12m value of column idad_12m.
     * @param idad15m value of column idad_15m.
     * @param idad4a value of column idad_4a.
     * @param idad11a14a value of column idad_11a14a.
     */
    public Idade(Long id, Vacina vacina, String idadNascer, String idad2m, String idad3m, String idad4m, String idad5m, String idad6m, String idad9m, String idad12m, String idad15m, String idad4a, String idad11a14a) {
        setId(id);
        setVacina(vacina);
        setIdadNascer(idadNascer);
        setIdad2m(idad2m);
        setIdad3m(idad3m);
        setIdad4m(idad4m);
        setIdad5m(idad5m);
        setIdad6m(idad6m);
        setIdad9m(idad9m);
        setIdad12m(idad12m);
        setIdad15m(idad15m);
        setIdad4a(idad4a);
        setIdad11a14a(idad11a14a);
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
     * Returns value of property {@link #vacina}.
     * @return value of property {@link #vacina}.
     */
    public Vacina getVacina() {
        return this.vacina;
    }
    /**
     * Sets new value of property {@link #vacina}.
     * @param vacina new value of property {@link #vacina}.
     */
    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }
    /**
     * Returns value of property {@link #idadNascer}.
     * @return value of property {@link #idadNascer}.
     */
    public String getIdadNascer() {
        return this.idadNascer;
    }
    /**
     * Sets new value of property {@link #idadNascer}.
     * @param idadNascer new value of property {@link #idadNascer}.
     */
    public void setIdadNascer(String idadNascer) {
        this.idadNascer = idadNascer;
    }
    /**
     * Returns value of property {@link #idad2m}.
     * @return value of property {@link #idad2m}.
     */
    public String getIdad2m() {
        return this.idad2m;
    }
    /**
     * Sets new value of property {@link #idad2m}.
     * @param idad2m new value of property {@link #idad2m}.
     */
    public void setIdad2m(String idad2m) {
        this.idad2m = idad2m;
    }
    /**
     * Returns value of property {@link #idad3m}.
     * @return value of property {@link #idad3m}.
     */
    public String getIdad3m() {
        return this.idad3m;
    }
    /**
     * Sets new value of property {@link #idad3m}.
     * @param idad3m new value of property {@link #idad3m}.
     */
    public void setIdad3m(String idad3m) {
        this.idad3m = idad3m;
    }
    /**
     * Returns value of property {@link #idad4m}.
     * @return value of property {@link #idad4m}.
     */
    public String getIdad4m() {
        return this.idad4m;
    }
    /**
     * Sets new value of property {@link #idad4m}.
     * @param idad4m new value of property {@link #idad4m}.
     */
    public void setIdad4m(String idad4m) {
        this.idad4m = idad4m;
    }
    /**
     * Returns value of property {@link #idad5m}.
     * @return value of property {@link #idad5m}.
     */
    public String getIdad5m() {
        return this.idad5m;
    }
    /**
     * Sets new value of property {@link #idad5m}.
     * @param idad5m new value of property {@link #idad5m}.
     */
    public void setIdad5m(String idad5m) {
        this.idad5m = idad5m;
    }
    /**
     * Returns value of property {@link #idad6m}.
     * @return value of property {@link #idad6m}.
     */
    public String getIdad6m() {
        return this.idad6m;
    }
    /**
     * Sets new value of property {@link #idad6m}.
     * @param idad6m new value of property {@link #idad6m}.
     */
    public void setIdad6m(String idad6m) {
        this.idad6m = idad6m;
    }
    /**
     * Returns value of property {@link #idad9m}.
     * @return value of property {@link #idad9m}.
     */
    public String getIdad9m() {
        return this.idad9m;
    }
    /**
     * Sets new value of property {@link #idad9m}.
     * @param idad9m new value of property {@link #idad9m}.
     */
    public void setIdad9m(String idad9m) {
        this.idad9m = idad9m;
    }
    /**
     * Returns value of property {@link #idad12m}.
     * @return value of property {@link #idad12m}.
     */
    public String getIdad12m() {
        return this.idad12m;
    }
    /**
     * Sets new value of property {@link #idad12m}.
     * @param idad12m new value of property {@link #idad12m}.
     */
    public void setIdad12m(String idad12m) {
        this.idad12m = idad12m;
    }
    /**
     * Returns value of property {@link #idad15m}.
     * @return value of property {@link #idad15m}.
     */
    public String getIdad15m() {
        return this.idad15m;
    }
    /**
     * Sets new value of property {@link #idad15m}.
     * @param idad15m new value of property {@link #idad15m}.
     */
    public void setIdad15m(String idad15m) {
        this.idad15m = idad15m;
    }
    /**
     * Returns value of property {@link #idad4a}.
     * @return value of property {@link #idad4a}.
     */
    public String getIdad4a() {
        return this.idad4a;
    }
    /**
     * Sets new value of property {@link #idad4a}.
     * @param idad4a new value of property {@link #idad4a}.
     */
    public void setIdad4a(String idad4a) {
        this.idad4a = idad4a;
    }
    /**
     * Returns value of property {@link #idad11a14a}.
     * @return value of property {@link #idad11a14a}.
     */
    public String getIdad11a14a() {
        return this.idad11a14a;
    }
    /**
     * Sets new value of property {@link #idad11a14a}.
     * @param idad11a14a new value of property {@link #idad11a14a}.
     */
    public void setIdad11a14a(String idad11a14a) {
        this.idad11a14a = idad11a14a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Idade idade = (Idade) o;

        if (id != null ? !id.equals(idade.id) : idade.id != null) {
            return false;
        }
        if (vacina != null ? !vacina.equals(idade.vacina) : idade.vacina != null) {
            return false;
        }
        if (idadNascer != null ? !idadNascer.equals(idade.idadNascer) : idade.idadNascer != null) {
            return false;
        }
        if (idad2m != null ? !idad2m.equals(idade.idad2m) : idade.idad2m != null) {
            return false;
        }
        if (idad3m != null ? !idad3m.equals(idade.idad3m) : idade.idad3m != null) {
            return false;
        }
        if (idad4m != null ? !idad4m.equals(idade.idad4m) : idade.idad4m != null) {
            return false;
        }
        if (idad5m != null ? !idad5m.equals(idade.idad5m) : idade.idad5m != null) {
            return false;
        }
        if (idad6m != null ? !idad6m.equals(idade.idad6m) : idade.idad6m != null) {
            return false;
        }
        if (idad9m != null ? !idad9m.equals(idade.idad9m) : idade.idad9m != null) {
            return false;
        }
        if (idad12m != null ? !idad12m.equals(idade.idad12m) : idade.idad12m != null) {
            return false;
        }
        if (idad15m != null ? !idad15m.equals(idade.idad15m) : idade.idad15m != null) {
            return false;
        }
        if (idad4a != null ? !idad4a.equals(idade.idad4a) : idade.idad4a != null) {
            return false;
        }
        if (idad11a14a != null ? !idad11a14a.equals(idade.idad11a14a) : idade.idad11a14a != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (vacina != null ? vacina.hashCode() : 0);
        result = 31 * result + (idadNascer != null ? idadNascer.hashCode() : 0);
        result = 31 * result + (idad2m != null ? idad2m.hashCode() : 0);
        result = 31 * result + (idad3m != null ? idad3m.hashCode() : 0);
        result = 31 * result + (idad4m != null ? idad4m.hashCode() : 0);
        result = 31 * result + (idad5m != null ? idad5m.hashCode() : 0);
        result = 31 * result + (idad6m != null ? idad6m.hashCode() : 0);
        result = 31 * result + (idad9m != null ? idad9m.hashCode() : 0);
        result = 31 * result + (idad12m != null ? idad12m.hashCode() : 0);
        result = 31 * result + (idad15m != null ? idad15m.hashCode() : 0);
        result = 31 * result + (idad4a != null ? idad4a.hashCode() : 0);
        result = 31 * result + (idad11a14a != null ? idad11a14a.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Idade{"
                + "id='" + id + '\''
                + ", vacina='" + vacina + '\''
                + ", idadNascer='" + idadNascer + '\''
                + ", idad2m='" + idad2m + '\''
                + ", idad3m='" + idad3m + '\''
                + ", idad4m='" + idad4m + '\''
                + ", idad5m='" + idad5m + '\''
                + ", idad6m='" + idad6m + '\''
                + ", idad9m='" + idad9m + '\''
                + ", idad12m='" + idad12m + '\''
                + ", idad15m='" + idad15m + '\''
                + ", idad4a='" + idad4a + '\''
                + ", idad11a14a='" + idad11a14a + '\''
                + '}';
    }
}

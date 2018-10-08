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
    private String vaciDescricao;
    private String vaciAdministracao;

    /**
     * Default constructor.
     */
    public Vacina() {
    }

    /**
     * All columns constructor.
     * @param id value of column vaci_id.
     * @param vaciNome value of column vaci_nome.
     * @param vaciDescricao value of column vaci_descricao.
     * @param vaciAdministracao value of column vaci_administracao.
     */
    public Vacina(Long id, String vaciNome, String vaciDescricao, String vaciAdministracao) {
        setId(id);
        setVaciNome(vaciNome);
        setVaciDescricao(vaciDescricao);
        setVaciAdministracao(vaciAdministracao);
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
     * Returns value of property {@link #vaciNome}.
     * @return value of property {@link #vaciNome}.
     */
    public String getVaciNome() {
        return this.vaciNome;
    }
    /**
     * Sets new value of property {@link #vaciNome}.
     * @param vaciNome new value of property {@link #vaciNome}.
     */
    public void setVaciNome(String vaciNome) {
        this.vaciNome = vaciNome;
    }
    /**
     * Returns value of property {@link #vaciDescricao}.
     * @return value of property {@link #vaciDescricao}.
     */
    public String getVaciDescricao() {
        return this.vaciDescricao;
    }
    /**
     * Sets new value of property {@link #vaciDescricao}.
     * @param vaciDescricao new value of property {@link #vaciDescricao}.
     */
    public void setVaciDescricao(String vaciDescricao) {
        this.vaciDescricao = vaciDescricao;
    }
    /**
     * Returns value of property {@link #vaciAdministracao}.
     * @return value of property {@link #vaciAdministracao}.
     */
    public String getVaciAdministracao() {
        return this.vaciAdministracao;
    }
    /**
     * Sets new value of property {@link #vaciAdministracao}.
     * @param vaciAdministracao new value of property {@link #vaciAdministracao}.
     */
    public void setVaciAdministracao(String vaciAdministracao) {
        this.vaciAdministracao = vaciAdministracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacina vacina = (Vacina) o;

        if (id != null ? !id.equals(vacina.id) : vacina.id != null) {
            return false;
        }
        if (vaciNome != null ? !vaciNome.equals(vacina.vaciNome) : vacina.vaciNome != null) {
            return false;
        }
        if (vaciDescricao != null ? !vaciDescricao.equals(vacina.vaciDescricao) : vacina.vaciDescricao != null) {
            return false;
        }
        if (vaciAdministracao != null ? !vaciAdministracao.equals(vacina.vaciAdministracao) : vacina.vaciAdministracao != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (vaciNome != null ? vaciNome.hashCode() : 0);
        result = 31 * result + (vaciDescricao != null ? vaciDescricao.hashCode() : 0);
        result = 31 * result + (vaciAdministracao != null ? vaciAdministracao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{"
                + "Código: '" + id + '\''
                + ", vaciNome: '" + vaciNome + '\''
                + ", vaciDescricao: '" + vaciDescricao + '\''
                + ", vaciAdministracao: '" + vaciAdministracao + '\''
                + '}';
    }

}

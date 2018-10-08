package br.com.erivando.vacinaskids.database.model;

import java.util.ArrayList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:39
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ControleDataModel {

    private String headerTitulo;
    private ArrayList<Vacina> itensInSection;

    public ControleDataModel() {
    }

    public ControleDataModel(String headerTitulo, ArrayList<Vacina> itensInSection) {
        this.headerTitulo = headerTitulo;
        this.itensInSection = itensInSection;
    }

    public String getHeaderTitulo() {
        return headerTitulo;
    }

    public void setHeaderTitulo(String headerTitulo) {
        this.headerTitulo = headerTitulo;
    }

    public ArrayList<Vacina> getItensInSection() {
        return itensInSection;
    }

    public void setItensInSection(ArrayList<Vacina> itensInSection) {
        this.itensInSection = itensInSection;
    }
}

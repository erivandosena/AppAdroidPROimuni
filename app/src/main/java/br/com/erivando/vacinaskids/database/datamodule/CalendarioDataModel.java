package br.com.erivando.vacinaskids.database.datamodule;

import java.util.ArrayList;

import br.com.erivando.vacinaskids.database.model.Vacina;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:39
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CalendarioDataModel {

    private String headerTitulo;
    private ArrayList<Vacina> itensInSection;

    public CalendarioDataModel() {
    }

    public CalendarioDataModel(String headerTitulo, ArrayList<Vacina> itensInSection) {
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

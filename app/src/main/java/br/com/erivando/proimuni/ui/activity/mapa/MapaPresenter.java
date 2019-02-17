package br.com.erivando.proimuni.ui.activity.mapa;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   24 de Outubro de 2018 as 23:42
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class MapaPresenter<V extends MapaMvpView> extends BasePresenter<V> implements MapaMvpPresenter<V> {

    @Inject
    public MapaPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

}

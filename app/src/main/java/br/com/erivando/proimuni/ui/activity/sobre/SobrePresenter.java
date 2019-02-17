package br.com.erivando.proimuni.ui.activity.sobre;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Julho de 2018 as 10:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class SobrePresenter<V extends SobreMvpView> extends BasePresenter<V> implements SobreMvpPresenter<V> {

    @Inject
    public SobrePresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getMvpView().updateAppVersion();
    }

}

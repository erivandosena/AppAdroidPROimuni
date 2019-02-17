package br.com.erivando.proimuni.ui.activity.introducao;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Janeiro de 2019 as 01:16
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class IntroducaoPresenter<V extends IntroducaoMvpView> extends BasePresenter<V> implements IntroducaoMvpPresenter<V> {

    @Inject
    public IntroducaoPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public boolean onIsIntroLaunch() {
        return getIDataManager().isFirstLaunch();
    }

    @Override
    public void onSetIntroLaunch(boolean isFirst) {
        getIDataManager().setFirstLaunch(isFirst);
    }
}


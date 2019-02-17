package br.com.erivando.proimuni.ui.activity.curiosidade;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Janeiro de 2019 as 21:18
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CuriosidadePresenter<V extends CuriosidadeMvpView> extends BasePresenter<V> implements CuriosidadeMvpPresenter<V> {

    @Inject
    public CuriosidadePresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

}


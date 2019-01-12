package br.com.erivando.proimuni.ui.activity.curiosidade;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;
import br.com.erivando.proimuni.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Janeiro de 2019 as 21:18
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CuriosidadePresenter<V extends CuriosidadeMvpView> extends BasePresenter<V> implements CuriosidadeMvpPresenter<V> {

    @Inject
    public CuriosidadePresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

}


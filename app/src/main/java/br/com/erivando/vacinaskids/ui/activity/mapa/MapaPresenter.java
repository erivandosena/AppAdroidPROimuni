package br.com.erivando.vacinaskids.ui.activity.mapa;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   24 de Outubro de 2018 as 23:42
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class MapaPresenter<V extends MapaMvpView> extends BasePresenter<V> implements MapaMvpPresenter<V> {

    @Inject
    public MapaPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

}

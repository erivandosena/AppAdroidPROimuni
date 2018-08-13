package br.com.erivando.vacinaskids.ui.acoes.cartao;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoPresenter<V extends CartaoMvpView> extends BasePresenter<V> implements CartaoMvpPresenter<V> {

    @Inject
    public CartaoPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }
}

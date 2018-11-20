package br.com.erivando.vacinaskids.ui.activity.notificacao;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Novembro de 2018 as 11:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


public class NotificacaoPresenter<V extends NotificacaoMvpView> extends BasePresenter<V> implements NotificacaoMvpPresenter<V> {

    @Inject
    public NotificacaoPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }
}

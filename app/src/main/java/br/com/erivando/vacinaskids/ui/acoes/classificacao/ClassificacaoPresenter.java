package br.com.erivando.vacinaskids.ui.acoes.classificacao;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ClassificacaoPresenter<V extends ClassificacaoMvpView> extends BasePresenter<V> implements ClassificacaoMvpPresenter<V> {
    public ClassificacaoPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }
}

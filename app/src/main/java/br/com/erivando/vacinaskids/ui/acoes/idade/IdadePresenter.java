package br.com.erivando.vacinaskids.ui.acoes.idade;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:58
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class IdadePresenter<V extends IdadeMvpView> extends BasePresenter<V> implements IdadeMvpPresenter<V> {

    @Inject
    public IdadePresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public List<Idade> onIdadesCadastradas() {
        return getIDataManager().obtemTodasIdades();
    }
}

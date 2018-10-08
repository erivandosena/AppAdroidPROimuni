package br.com.erivando.vacinaskids.ui.acoes.vacina;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaPresenter<V extends VacinaMvpView> extends BasePresenter<V> implements VacinaMvpPresenter<V> {

    @Inject
    public VacinaPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public List<Vacina> onVacinasCadastradas() {
        return getIDataManager().obtemTodasVacinas();
    }
}

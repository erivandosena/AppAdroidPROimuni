package br.com.erivando.vacinaskids.ui.acoes.dose;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:02
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class DosePresenter<V extends DoseMvpView> extends BasePresenter<V> implements DoseMvpPresenter<V> {

    @Inject
    public DosePresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public List<Dose> onDosesCadastradas() {
        return getIDataManager().obtemTodasDoses();
    }
}

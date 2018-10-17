package br.com.erivando.vacinaskids.ui.activity.calendario;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 00:49
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CalendarioPresenter<V extends CalendarioMvpView> extends BasePresenter<V> implements CalendarioMvpPresenter<V> {

    @Inject
    public CalendarioPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }
    @Override
    public List<Calendario> onCalendariosCadastrados() {
        return getIDataManager().obtemCalendarios();
    }

    @Override
    public Calendario onCalendarioCadastrado() {
        return getIDataManager().obtemCalendario();
    }

}

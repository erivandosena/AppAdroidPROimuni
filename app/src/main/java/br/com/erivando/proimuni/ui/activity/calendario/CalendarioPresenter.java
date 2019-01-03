package br.com.erivando.proimuni.ui.activity.calendario;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.mvp.base.BasePresenter;
import br.com.erivando.proimuni.util.rx.SchedulerProvider;
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
        return getIDataManager().obtemCalendariosOrderBy("vacina.id");
    }

    @Override
    public List<Calendario> onCalendariosPorVacina(String[] valores) {
        return getIDataManager().obtemCalendariosPorVacina(valores);
    }

    @Override
    public List<Calendario> onCalendariosPorNomeVacina(String[] valores) {
        return getIDataManager().obtemCalendarios(valores);
    }

    @Override
    public Calendario onCalendarioCadastrado() {
        return getIDataManager().obtemCalendario();
    }

}

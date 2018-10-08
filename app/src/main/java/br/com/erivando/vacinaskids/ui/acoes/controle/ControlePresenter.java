package br.com.erivando.vacinaskids.ui.acoes.controle;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Controle;
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

public class ControlePresenter<V extends ControleMvpView> extends BasePresenter<V> implements ControleMvpPresenter<V> {

    @Inject
    public ControlePresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }
    @Override
    public List<Controle> onControlesCadastrados() {
        return getIDataManager().obtemTodosControles();
    }

    @Override
    public Controle onControleCadastrado() {
        return getIDataManager().obtemControle();
    }

}

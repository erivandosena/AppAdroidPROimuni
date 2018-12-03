package br.com.erivando.proimuni.mvp.base;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 16:28
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.MvpPresenter;
import br.com.erivando.proimuni.mvp.MvpView;
import br.com.erivando.proimuni.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Classe base que implementa a interface do Presenter e fornece uma implementação básica para
 * onAttach() e onDetach(). Ele também lida com uma referência ao mvpView que pode ser acessado
 * a partir das classes filhas chamando getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final IDataManager iDataManager;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    private V mvpView;

    @Inject
    public BasePresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        this.iDataManager = iDataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        mvpView = null;
    }

    protected boolean isViewAttached() {
        return mvpView != null;
    }

    protected V getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    protected IDataManager getIDataManager() {
        return iDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void setUserAsLoggedOut() {
        getIDataManager().setAccessToken(null);
    }

    static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Por favor, ligue ao Presenter.onAttach (MvpView) antes de" +
                    "solicitar dados ao apresentador");
        }
    }


}

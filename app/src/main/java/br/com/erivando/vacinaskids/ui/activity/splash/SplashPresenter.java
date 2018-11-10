package br.com.erivando.vacinaskids.ui.activity.splash;

import android.os.Handler;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static br.com.erivando.vacinaskids.database.IDataManager.LoggedInMode.LOGGED_IN_MODE_LOCAL;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:18
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V>, Runnable {

    @Inject
    public SplashPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        getMvpView().startServico();

        Handler handler = new Handler();
        handler.postDelayed(this, 1500);
        importaDadosJson();
    }

    private void decideNextActivity() {
        if (getIDataManager().getCurrentUserLoggedInMode() == IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getMvpView().openLoginActivity();
        } else {
            if (getMvpView().isNetworkConnected()) {
                getMvpView().openMainActivity();
            } else {
                if (getIDataManager().getCurrentUserLoggedInMode() == LOGGED_IN_MODE_LOCAL.getType()) {
                    getMvpView().openMainActivity();
                } else {
                    getIDataManager().setUserAsLoggedOut();
                    getMvpView().openLoginActivity();
                }
            }
        }
    }

    @Override
    public void run() {
        decideNextActivity();
    }

    @Override
    public void importaDadosJson() {
        getIDataManager().getRecursosJson();
    }
}

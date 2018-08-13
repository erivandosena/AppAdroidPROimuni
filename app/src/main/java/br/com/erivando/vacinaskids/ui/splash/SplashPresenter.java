package br.com.erivando.vacinaskids.ui.splash;

import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

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
        Handler handler = new Handler();
        handler.postDelayed(this, 2 * 1000);
    }

    private void decideNextActivity() {
        Log.d("UserLoggedInMode", String. valueOf(getIDataManager().getCurrentUserLoggedInMode()));
        Log.d("LOGGED_IN_MODE", String. valueOf(IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()));
        if (getIDataManager().getCurrentUserLoggedInMode() == IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getMvpView().openLoginActivity();
        } else {
            getMvpView().openMainActivity();
        }
    }

    @Override
    public void run() {
        decideNextActivity();
    }

}

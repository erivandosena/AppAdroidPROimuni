package br.com.erivando.vacinaskids.mvp.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.ViewGroup;

import br.com.erivando.vacinaskids.mvp.MvpView;
import br.com.erivando.vacinaskids.mvp.MvpViewExtra;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 17:38
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public abstract class BaseViewExtra extends ViewGroup implements MvpViewExtra {

    private MvpView parentMvpView;

    public BaseViewExtra(Context context) {
        super(context);
    }

    public BaseViewExtra(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseViewExtra(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseViewExtra(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void attachParentMvpView(MvpView mvpView) {
        parentMvpView = mvpView;
    }

    @Override
    public void showLoading() {
        if (parentMvpView != null) {
            parentMvpView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (parentMvpView != null) {
            parentMvpView.hideLoading();
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (parentMvpView != null) {
            parentMvpView.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (parentMvpView != null) {
            parentMvpView.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (parentMvpView != null) {
            parentMvpView.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (parentMvpView != null) {
            parentMvpView.showMessage(resId);
        }
    }

    @Override
    public void hideKeyboard() {
        if (parentMvpView != null) {
            parentMvpView.hideKeyboard();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return parentMvpView != null && parentMvpView.isNetworkConnected();
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (parentMvpView != null) {
            parentMvpView.openActivityOnTokenExpire();
        }
    }

    protected abstract void bindViewsAndSetOnClickListeners();

    protected abstract void setUp();

}
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

    private MvpView mParentMvpView;

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
        mParentMvpView = mvpView;
    }

    @Override
    public void showLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.hideLoading();
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (mParentMvpView != null) {
            mParentMvpView.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mParentMvpView != null) {
            mParentMvpView.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.showMessage(resId);
        }
    }

    @Override
    public void hideKeyboard() {
        if (mParentMvpView != null) {
            mParentMvpView.hideKeyboard();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mParentMvpView != null) {
            return mParentMvpView.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mParentMvpView != null) {
            mParentMvpView.openActivityOnTokenExpire();
        }
    }

    protected abstract void bindViewsAndSetOnClickListeners();

    protected abstract void setUp();
}
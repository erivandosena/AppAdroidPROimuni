package br.com.erivando.proimuni.mvp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import br.com.erivando.proimuni.di.component.ActivityComponent;
import br.com.erivando.proimuni.mvp.MvpView;
import br.com.erivando.proimuni.util.CommonUtils;
import butterknife.Unbinder;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   25 de Outubro de 2018 as 20:03
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements MvpView {

    private BaseActivity baseActivity;
    private Unbinder unBinder;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = CommonUtils.showLoadingDialog(getContextActivity());
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (baseActivity != null) {
            baseActivity.openActivityOnTokenExpire();
        }
    }

    @Override
    public void onError(int resId) {
        if (baseActivity != null) {
            baseActivity.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (baseActivity != null) {
            baseActivity.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (baseActivity != null) {
            baseActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(int resId) {
        if (baseActivity != null) {
            baseActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return baseActivity != null && baseActivity.isNetworkConnected();
    }

    @Override
    public void hideKeyboard() {
        if (baseActivity != null) {
            baseActivity.hideKeyboard();
        }
    }

    @Override
    public Context getContextActivity() {
        return getBaseContext();
    }

    @Override
    public void onDestroy() {
        if (unBinder != null) {
            unBinder.unbind();
        }
        super.onDestroy();
    }

    protected ActivityComponent getActivityComponent() {
        if (baseActivity != null) {
            return baseActivity.getActivityComponent();
        }
        return null;
    }

    protected BaseActivity getBaseActivity() {
        return baseActivity;
    }

    protected void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    protected abstract void setUp(View view);
}

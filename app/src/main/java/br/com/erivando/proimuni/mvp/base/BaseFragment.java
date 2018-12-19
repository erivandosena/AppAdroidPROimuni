package br.com.erivando.proimuni.mvp.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import br.com.erivando.proimuni.di.component.ActivityComponent;
import br.com.erivando.proimuni.di.component.DaggerActivityComponent;
import br.com.erivando.proimuni.di.module.ActivityModule;
import br.com.erivando.proimuni.mvp.MvpView;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import br.com.erivando.proimuni.util.CommonUtils;
import butterknife.Unbinder;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 15:12
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public abstract class BaseFragment extends Fragment implements MvpView {

    private BaseActivity baseActivity;
    private Unbinder unBinder;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 22)
            baseActivity.setStatusBarTranslucent(false);
        else
            baseActivity.setStatusBarTranslucent(true);

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);

        if (Build.VERSION.SDK_INT >= 19) {
            baseActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            baseActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


    }

    public static void setWindowFlag(Activity activity, final int bits, boolean state){
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();

        if (state)
        {
            winParams.flags |= bits;
        }
        else
        {
            winParams.flags &= ~bits;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.baseActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = CommonUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onError(String message) {
        if (baseActivity != null) {
            baseActivity.onError(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (baseActivity != null) {
            baseActivity.onError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (baseActivity != null) {
            baseActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (baseActivity != null) {
            baseActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return baseActivity != null && baseActivity.isNetworkConnected();
    }

    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (baseActivity != null) {
            baseActivity.hideKeyboard();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (baseActivity != null) {
            baseActivity.openActivityOnTokenExpire();
        }
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

    @Override
    public void onDestroy() {
        if (unBinder != null) {
            unBinder.unbind();
        }
        super.onDestroy();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}

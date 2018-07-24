package br.com.erivando.vacinaskids.ui.login;

import android.util.Log;

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.DataManager;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.api.LoginRequest;
import br.com.erivando.vacinaskids.database.api.LoginResponse;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:17
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onLoginClick(String login, String senha) {
        //validate email and password
        if (login == null || login.isEmpty()) {
            getMvpView().onError(R.string.text_valida_login);
            return;
        }
        /*
        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.invalid_email);
            return;
        }
        */
        if (senha == null || senha.isEmpty()) {
            getMvpView().onError(R.string.text_valida_senha);
            return;
        }
        getMvpView().showLoading();

        Log.d("validaLoginUsuario", String.valueOf(getIDataManager().validaLoginUsuario(login, senha)) );

        try {
            if (getIDataManager().validaLoginUsuario(login, senha)) {

                Usuario usuario = new Usuario();
                usuario = getIDataManager().obtemUsuario(new String[]{"usuaLogin",login}, new String[]{"usuaSenha",senha});
                usuario.getId();
                usuario.getUsuaNome();
                usuario.getUsuaLogin();
                usuario.getUsuaSenha();
                usuario.getUsuaEmail();

                getIDataManager().updateUserInfo(
                        null,
                        Long.valueOf(usuario.getId()),
                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOCAL,
                        usuario.getUsuaNome(),
                        usuario.getUsuaEmail(),
                        null
                        /*
                        response.getAccessToken(),
                        response.getUserId(),
                        DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                        response.getUserName(),
                        response.getUserEmail(),
                        response.getGoogleProfilePicUrl()
                        */
                        );
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().openMainActivity();
            } else {
                getMvpView().onError(R.string.text_valida_usuario);
            }
        } finally {
            getMvpView().hideLoading();
        }

    }

    @Override
    public void onGoogleLoginClick() {
        // instruir o LoginActivity para iniciar o login do google
        getMvpView().showLoading();

        getMvpView().showMessage("Ainda não implementado! :(");

        getMvpView().hideLoading();
    }

    @Override
    public void onFacebookLoginClick() {
        //instruir o LoginActivity para iniciar o login do facebook
        getMvpView().showLoading();

        getMvpView().showMessage("Ainda não implementado! :(");

        getMvpView().hideLoading();

    }

    @Override
    public void onCadstroUsuarioClick() {
        getMvpView().openCadastroUsuarioActivity();
    }

}

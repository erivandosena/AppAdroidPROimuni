package br.com.erivando.vacinaskids.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:16
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void onCreateLocalLogin(String login, String senha);

    void onCreateFacebookLogin();

    void onCreateGoogleLogin();

    CallbackManager getCallbackManager();

    AccessTokenTracker getAccessTokenTracker();

    Bundle getParametrosFacebook(JSONObject jsonObject);

    GoogleApiClient getGoogleApiClient();

    void getHandleActivityResult(int requestCode, int resultCode, Intent data);

    //void handleSignInResult(GoogleSignInResult result);
    void handleSignInResult(Task<GoogleSignInAccount> completedTask);

    void onGooleSignOut(Activity activity);

    void onVerificaLoginGoogle();

    GoogleSignInClient getGoogleSignInClient();
}

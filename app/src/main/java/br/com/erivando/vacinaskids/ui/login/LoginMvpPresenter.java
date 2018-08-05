package br.com.erivando.vacinaskids.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

    Bundle getParametrosFacebook(JSONObject jsonObject);

    void getActivityResult(int requestCode, int resultCode, Intent data);

    void handleSignInResult(Task<GoogleSignInAccount> completedTask);

    void onGooleSignOut(Activity activity);

    boolean onVerificaUsuarioCadastrado();

    GoogleSignInClient getGoogleSignInClient();

    void enviaSenhaPorEmail(View view, String login);

    void getRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults);

    void chooseAccount(View view);

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode);

    void acquireGooglePlayServices();

    boolean isGooglePlayServicesAvailable();

    void getResultsFromApi(View view);

    void onCreateGoogleAccountCredential();
}

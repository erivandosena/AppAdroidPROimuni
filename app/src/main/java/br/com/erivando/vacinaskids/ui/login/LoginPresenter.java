package br.com.erivando.vacinaskids.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.DataManager;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:17
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V>, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginPresenter";

    /* Login facebook */
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private boolean isLoggedIn;

    /* Login google */
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;
    public static final int RC_SIGN_IN = 777;
    private GoogleSignInClient googleSignInClient;

    @Inject
    public LoginPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);

    }

    @Override
    public void onCreateLocalLogin(String login, String senha) {
        //validando login do usuário
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
    public void onCreateFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // Defina o token de acesso usando currentAccessToken quando ele for carregado ou configurado.
                if (oldAccessToken != currentAccessToken)
                    accessToken = currentAccessToken;
            }
        };

        isLoggedIn = accessToken != null && !accessToken.isExpired();

        try {
            getMvpView().showLoading();
            if (isLoggedIn) {
                getMvpView().openMainActivity();
            } else {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Se o token de acesso já estiver disponível, atribua-o.
                        accessToken = loginResult.getAccessToken();
                        accessToken.getPermissions();

                        //Toast.makeText(getApplicationContext(), "TOKEN:\n" + loginResult.getAccessToken().getToken().toString(), Toast.LENGTH_SHORT).show();

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                if (graphResponse.getError() == null) {
                                    final Bundle bundleData = getParametrosFacebook(jsonObject);
                                    // getDadosPerfilFacebook(accessToken, bundleJsonObject);
                                    GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                            try {
                                                if (graphResponse.getError() == null && bundleData != null) {
                                                    Log.d("Dados BÁSICOS", graphResponse.toString());
                                                    String id = bundleData.getString("id");
                                                    String nome = bundleData.getString("name");
                                                    String email = bundleData.getString("email");
                                                    String primeiroNome = bundleData.getString("first_name");
                                                    String segundoNome = bundleData.getString("last_name");
                                                    Log.d("Dados EXTRAS", bundleData.toString());
                                                    String genero = bundleData.getString("user_gender");
                                                    String aniversario = bundleData.getString("user_birthday");
                                                    String fotos = bundleData.getString("user_photos");
                                                    String localizacao = bundleData.getString("user_location");
                                                    URL imagemPerfil = new URL("https://graph.facebook.com/" + id + "/picture?type=large");

                                                    getIDataManager().updateUserInfo(
                                                            accessToken.getToken(),
                                                            Long.valueOf(id),
                                                            IDataManager.LoggedInMode.LOGGED_IN_MODE_FACEBOOK,
                                                            (!nome.equals(primeiroNome + " " + segundoNome) ? nome : primeiroNome + " " + segundoNome),
                                                            email,
                                                            (imagemPerfil != null) ? imagemPerfil.toString() : null
                                                    );
                                                }
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } finally {
                                                getMvpView().openMainActivity();
                                            }
                                        }
                                    }).executeAsync();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                    /* obrigatório solicitar parametros extras ao Facebook
                    parameters.putString("fields", "id, email, name, first_name, last_name, user_gender, user_photos, user_birthday, user_location");
                    */
                        parameters.putString("fields", "id, email, name, first_name, last_name");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        getMvpView().hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.facebook_cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getMvpView().hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.facebook_error_fabebook_login, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } finally {
            getMvpView().hideLoading();
        }
    }

    @Override
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    public Bundle getParametrosFacebook(JSONObject jsonObject) {
        Bundle bundle = new Bundle();
        try {
            if (jsonObject != null) {
                /* Parâmetros default*/
                if (jsonObject.has("id"))
                    bundle.putString("id", jsonObject.getString("id"));
                if (jsonObject.has("name"))
                    bundle.putString("name", jsonObject.getString("name"));
                if (jsonObject.has("email"))
                    bundle.putString("email", jsonObject.getString("email"));
                /* Parâmetros extras solicitados ao facebook*/
                if (jsonObject.has("user_gender"))
                    bundle.putString("user_gender", jsonObject.getString("user_gender"));
                if (jsonObject.has("user_birthday"))
                    bundle.putString("user_birthday", jsonObject.getString("user_birthday"));
                if (jsonObject.has("user_photos"))
                    bundle.putString("user_photos", jsonObject.getString("user_photos"));
                if (jsonObject.has("user_location"))
                    bundle.putString("user_location", jsonObject.getJSONObject("user_location").getString("name"));
            }
            return bundle;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreateGoogleLogin() {
       // GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       // this.googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(new FragmentActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
      //  setGoogleApiClient(new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(new FragmentActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build());


        // Configure o login para solicitar o ID do usuário, o endereço de e-mail e o perfil básico.
        // O ID e o perfil básico estão incluídos em DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Construa um GoogleSignInClient com as opções especificadas pelo gso.
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        if (googleSignInClient == null)
            onCreateGoogleLogin();
        return googleSignInClient;
    }

    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
        this.googleSignInClient = googleSignInClient;
    }

    @Override
    public AccessTokenTracker getAccessTokenTracker() {
        return accessTokenTracker;
    }

    public GoogleApiClient getGoogleApiClient() {
        if (googleApiClient == null)
            onCreateGoogleLogin();
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), R.string.google_falha_conexao, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getHandleActivityResult(int requestCode, int resultCode, Intent data) {
       // if (requestCode == SIGN_IN_CODE) {
        //    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        //    handleSignInResult(result);
        //}

        // Resultado retornado do lançamento do Intent do GoogleSignInClient.getSignInIntent (...);
        if (requestCode == RC_SIGN_IN) {
            // A tarefa retornada dessa chamada está sempre concluída, não é necessário anexar um
            // listener (ouvinte).
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Conectado com sucesso, mostre a interface do usuário autenticada.
            getIDataManager().updateUserInfo(
                    account.getIdToken(),
                    Double.valueOf(account.getId()).longValue(),
                    IDataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                    account.getDisplayName(),
                    account.getEmail(),
                    (account.getPhotoUrl() != null) ?  account.getPhotoUrl().toString() : null
            );
            getMvpView().openMainActivity();

        } catch (ApiException e) {
            getMvpView().hideLoading();
            // O código de status ApiException indica o motivo detalhado da falha.
            // Por favor, consulte a referência da classe GoogleSignInStatusCodes para
            // mais informações. e.getStatusCode()
            Toast.makeText(getApplicationContext(), R.string.google_cancel_login, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    @Override
    public void handleSignInResult(GoogleSignInResult result) {

        Log.d("result",result.toString());
        Log.d("result.getStatus()",result.getStatus().toString());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            getIDataManager().updateUserInfo(
                    account.getIdToken(),
                    Double.valueOf(account.getId()).longValue(),
                    IDataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                    account.getDisplayName(),
                    account.getEmail(),
                    account.getPhotoUrl().toString()
            );
            getMvpView().openMainActivity();
        } else {
            getMvpView().hideLoading();
            Toast.makeText(getApplicationContext(), R.string.google_cancel_login, Toast.LENGTH_SHORT).show();
        }
    }
    */

    @Override
    public void onGooleSignOut(Activity activity) {
        googleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        /*
        Auth.GoogleSignInApi.signOut(getGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (!status.isSuccess()) {
                    Toast.makeText(getApplicationContext(), R.string.google_notlogout, Toast.LENGTH_SHORT).show();
                }
            }
        });

       */
    }

    @Override
    public void onVerificaLoginGoogle() {
        // Verifique a conta existente de login do Google, se o usuário já estiver conectado
        // o GoogleSignInAccount não será nulo.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
    }

}

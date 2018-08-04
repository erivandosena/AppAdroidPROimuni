package br.com.erivando.vacinaskids.ui.login;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.DataManager;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.InternetDetector;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;
import static br.com.erivando.vacinaskids.util.Uteis.base64ParaBitmap;
import static br.com.erivando.vacinaskids.util.Uteis.bitmapParaUri;
import static br.com.erivando.vacinaskids.util.Uteis.getPathFromUri;
import static br.com.erivando.vacinaskids.util.Uteis.hasPermissoes;



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
    private GoogleSignInClient googleSignInClient;
    public static final int RC_SIGN_IN = 777;
    /* GMail API OAuth 2.0 */
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS,GmailScopes.GMAIL_COMPOSE,GmailScopes.GMAIL_INSERT,GmailScopes.MAIL_GOOGLE_COM);
    private GoogleAccountCredential googleAccountCredential;
    private InternetDetector internetDetector;
    private FloatingActionButton floatingActionButton;
    //public String fileName = "";
    private Usuario usuario;
    private AlertDialog.Builder alertDialogBuilder;

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
        if (senha == null || senha.isEmpty()) {
            getMvpView().onError(R.string.text_valida_senha);
            return;
        }
        getMvpView().showLoading();

        try {
            if (getIDataManager().validaLoginUsuario(login, senha)) {
                Usuario usuario = getIDataManager().obtemUsuario(new String[]{"usuaLogin", login}, new String[]{"usuaSenha", senha});
                getIDataManager().updateUserInfo(
                        UUID.randomUUID().toString().replace("-",""),
                        Long.valueOf(usuario.getId()),
                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOCAL,
                        usuario.getUsuaNome(),
                        usuario.getUsuaEmail(),
                        (usuario.getUsuaFoto() != null) ? "file://"+getPathFromUri(getMvpView().getContextActivity(), bitmapParaUri(getMvpView().getContextActivity(),base64ParaBitmap(usuario.getUsuaFoto()))) : null
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

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                if (graphResponse.getError() == null) {
                                    final Bundle bundleData = getParametrosFacebook(jsonObject);
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
                        Toast.makeText(getMvpView().getContextActivity(), R.string.facebook_cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getMvpView().hideLoading();
                        Toast.makeText(getMvpView().getContextActivity(), R.string.facebook_error_fabebook_login, Toast.LENGTH_SHORT).show();
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
        // Configure o login para solicitar o ID do usuário, o endereço de e-mail e o perfil básico.
        // O ID e o perfil básico estão incluídos em DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Construa um GoogleSignInClient com as opções especificadas pelo gso.
        googleSignInClient = GoogleSignIn.getClient(getMvpView().getContextActivity(), gso);
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        if (googleSignInClient == null)
            onCreateGoogleLogin();
        return googleSignInClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getMvpView().getContextActivity(), R.string.google_falha_conexao, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getActivityResult(int requestCode, int resultCode, Intent data) {
        // Resultado retornado do lançamento do Intent do GoogleSignInClient.getSignInIntent (...);
        if (requestCode == RC_SIGN_IN) {
            // A tarefa retornada dessa chamada está sempre concluída, não é necessário anexar um listener (ouvinte).
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showMessage(floatingActionButton, "Este aplicativo requer o Google Play Services. Por favor instale " +
                            "Google Play Services no seu dispositivo e tente novamente.");
                } else {
                    getResultsFromApi(floatingActionButton);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getMvpView().getContextActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        googleAccountCredential.setSelectedAccountName(accountName);
                        getResultsFromApi(floatingActionButton);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi(floatingActionButton);
                }

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
                    (account.getPhotoUrl() != null) ? account.getPhotoUrl().toString() : null
            );
            getMvpView().openMainActivity();
        } catch (ApiException e) {
            // O código de status ApiException indica o motivo detalhado da falha.
            // Por favor, consulte a referência da classe GoogleSignInStatusCodes para
            // mais informações. e.getStatusCode()
            Toast.makeText(getMvpView().getContextActivity(), R.string.google_cancel_login, Toast.LENGTH_SHORT).show();
        } finally {
            getMvpView().hideLoading();
        }
    }

    @Override
    public void onGooleSignOut(Activity activity) {
        googleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    @Override
    public void onVerificaLoginGoogle() {
        // Verifique a conta existente de login do Google, se o usuário já estiver conectado
        // o GoogleSignInAccount não será nulo.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getMvpView().getContextActivity());
    }

    @Override
    public boolean onVerificaUsuarioCadastrado() {
        if(getIDataManager().getUsuarioID().get() == 0)
            return true;
        else
            return false;
    }

    @Override
    public void enviaSenhaPorEmail(View view, String login) {

        if (login == null || login.isEmpty() || login.length() < 4 || login.length() > 20 || login.matches("^[a-zA-Z]+ [a-zA-Z]+.*")) {
            getMvpView().onError(R.string.text_valida_login);
            return;
        }
        usuario = getIDataManager().obtemUsuario(new String[]{"usuaLogin",login});
        if (usuario == null) {
            getMvpView().onError(R.string.erro_text_usuario);
            return;
        }

        getResultsFromApi(view);
    }

    private void showMessage(View view, String message) {
        getMvpView().onError(message);
    }

    @Override
    public void getRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                chooseAccount(floatingActionButton);
                break;
            case 1:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getMvpView().getStartActivityForResult(photoPickerIntent, 1);
                break;

        }
    }

    @Override
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getMvpView().getContextActivity().getContentResolver().query(contentUri, proj, "", null, "");
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * Armazenando o ID do Mail usando Shared Preferences
     * @param view
     */
    @Override
    public void chooseAccount(View view) {
        if (hasPermissoes(getMvpView().getContextActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getMvpView().getContextActivity().getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                googleAccountCredential.setSelectedAccountName(accountName);
                getResultsFromApi(view);
            } else {
                // Inicie uma caixa de diálogo onde o usuário pode escolher uma conta
                getMvpView().getStartActivityForResult(googleAccountCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            ActivityCompat.requestPermissions(getMvpView().getContextActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
        }
    }

    /**
     * Método para informações de erro do Google Play Service
     * @param connectionStatusCode
     */
    @Override
    public void showGooglePlayServicesAvailabilityErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(getMvpView().getContextActivity(), connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Método para mostrar informações, se o Google Play Service não estiver disponível.
     */
    @Override
    public void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getMvpView().getContextActivity());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Armazenando o ID do Mail usando Shared Preferences
     * @return
     */
    @Override
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getMvpView().getContextActivity());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    @Override
    public void getResultsFromApi(View view) {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (googleAccountCredential.getSelectedAccountName() == null) {
            chooseAccount(view);
        } else if (!internetDetector.checkMobileInternetConn()) {
            showMessage(view, "Sem conexão com à Internet!");
        } else {
            new CredentialRequestTask(this, googleAccountCredential).execute();
        }
    }

    @Override
    public void onCreateGoogleAccountCredential() {
        // Inicializa Internet Checker
        internetDetector = new InternetDetector(getMvpView().getContextActivity());
        //Inicializa credenciais e objeto de serviço.
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(getMvpView().getContextActivity(), SCOPES).setBackOff(new ExponentialBackOff());

        alertDialogBuilder = new AlertDialog.Builder(getMvpView().getContextActivity());
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setNegativeButton("Ok", null);
        alertDialogBuilder.setCancelable(false);
    }

    // Async Task para enviar e-mails usando o OAuth do GMail
    private class CredentialRequestTask extends AsyncTask<Void, Void, String> {

        private Gmail service = null;
        private Exception lastError = null;
        private View view = floatingActionButton;
        private LoginActivity activity;

        public CredentialRequestTask(LoginPresenter<V> vLoginPresenter, GoogleAccountCredential googleAccountCredential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            service = new Gmail.Builder(transport, jsonFactory, googleAccountCredential)
                    .setApplicationName(getMvpView().getContextActivity().getResources().getString(R.string.app_name))
                    .build();
            this.activity = activity;
        }

        private String getDataFromApi() throws IOException {
            // Obtendo valores para, email, assunto e mensagem
            String user = "me";
            String to = usuario.getUsuaEmail();
            String from = googleAccountCredential.getSelectedAccountName();
            String subject = "Senha do app";
            String body =  "Prezado(a) "+usuario.getUsuaNome()+"\n\nSegue a senha solicitada: "+usuario.getUsuaSenha()+"\n\n© "+ Calendar.getInstance().get(Calendar.YEAR)+" "+getMvpView().getContextActivity().getResources().getString(R.string.app_name)+"\n"+getMvpView().getContextActivity().getResources().getString(R.string.app_slogan);
            MimeMessage mimeMessage;
            String response = "";
            try {
                mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(service, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return response;
        }

        // Método para envio do e-mail
        private String sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
            Message message = createMessageWithEmail(email);
            // Método oficial do GMail para enviar e-mail com OAuth 2.0
            message = service.users().messages().send(userId, message).execute();
            //System.out.println("Message id: " + message.getId());
            //System.out.println(message.toPrettyString());
            return message.getId();
        }

        // Método para criar e-mails Params
        private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress tAddress = new InternetAddress(to);
            InternetAddress fAddress = new InternetAddress(from);

            email.setFrom(fAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
            email.setSubject(subject);

            // Crie o objeto Multipart e adicione objetos MimeBodyPart a este objeto
            Multipart multipart = new MimeMultipart();

            BodyPart textBody = new MimeBodyPart();
            textBody.setText(bodyText);
            multipart.addBodyPart(textBody);

            /*
            if (!(activity.fileName.equals(""))) {
                // Crie um novo objeto MimeBodyPart e defina o objeto DataHandler para este objeto
                MimeBodyPart attachmentBody = new MimeBodyPart();
                String filename = activity.fileName; // change accordingly
                DataSource source = new FileDataSource(filename);
                attachmentBody.setDataHandler(new DataHandler(source));
                attachmentBody.setFileName(filename);
                multipart.addBodyPart(attachmentBody);
            }
            */

            //Definir o objeto multipart para o objeto de mensagem
            email.setContent(multipart);

            return email;
        }

        private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(Void... voids) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                lastError = e;
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            getMvpView().showLoading();
        }

        @Override
        protected void onPostExecute(String output) {
            getMvpView(). hideLoading();
            if (output == null || output.length() == 0) {
                showMessage(view, "Nenhum resultado retornado.");
            } else {
                alertDialogBuilder.setMessage("Senha enviada para a conta do e-mail:\n"+usuario.getUsuaEmail()+" \n\nVerifique sua caixa de entrada.");
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            getMvpView().hideLoading();
            if (lastError != null) {
                if (lastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) lastError).getConnectionStatusCode());
                } else if (lastError instanceof UserRecoverableAuthIOException) {
                    getMvpView().getStartActivityForResult(((UserRecoverableAuthIOException) lastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    showMessage(view, "Erro ao processar informações:\n" + lastError);
                }
            } else {
                showMessage(view, "Requisição cancelada.");
            }
        }
    }
}


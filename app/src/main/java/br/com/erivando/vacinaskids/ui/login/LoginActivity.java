package br.com.erivando.vacinaskids.ui.login;

import android.Manifest;
import android.accounts.AccountManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.cadastro.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import br.com.erivando.vacinaskids.util.InternetDetector;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.ui.login.LoginPresenter.RC_SIGN_IN;
import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;
import static br.com.erivando.vacinaskids.util.Uteis.hasPermissoes;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:16
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LoginActivity extends BaseActivity implements LoginMvpView {

    private static final String TAG = "LoginActivity";

    @Inject
    LoginMvpPresenter<LoginMvpView> presenter;

    @BindView(R.id.text_usua_login)
    EditText loginEditText;

    @BindView(R.id.text_usua_senha)
    EditText senhaEditText;

    @BindView(R.id.btn_facebook_login)
    ImageButton facebookLogin;

    @BindView(R.id.btn_google_login)
    ImageButton googleLogin;

    @BindView(R.id.tv_btn_lembrar_senha)
    TextView lembrarSenhaTextView;

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    GoogleAccountCredential googleAccountCredential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final List<String> SCOPES = Arrays.asList(
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            //GmailScopes.GMAIL_MODIFY,
            //GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    );
    //private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
    //private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/2/drive-java-quickstart.json");
    private InternetDetector internetDetector;
    FloatingActionButton floatingActionButton;
    private final int SELECT_PHOTO = 1;
    public String fileName = "";

    private static String login;
    private static Usuario usuario;

    private static AlertDialog.Builder alertDialogBuilder;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(LoginActivity.this);

        setUp();
    }

    @OnClick(R.id.btn_login_usuario)
    void onLoginLocalClick(View v) {
        presenter.onCreateLocalLogin(loginEditText.getText().toString(), senhaEditText.getText().toString());
    }

    @OnClick(R.id.btn_google_login)
    void onGoogleLoginClick(View v) {
        showLoading();
        if (v == googleLogin) {
            Intent signInIntent = presenter.getGoogleSignInClient().getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @OnClick(R.id.btn_facebook_login)
    void onFacebookLoginClick(View v) {
        showLoading();
        if (v == facebookLogin) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    @OnClick(R.id.tv_btn_lembrar_senha)
    void onLembrarSenhaClick(View v) {
        //presenter.enviaSenhaEmail(this, loginEditText.getText().toString());
        if (v == lembrarSenhaTextView) {

            login = loginEditText.getText().toString();

            if (login == null || login.isEmpty() || login.length() < 4 || login.length() > 20 || login.matches("^[a-zA-Z]+ [a-zA-Z]+.*")) {
                onError(R.string.text_valida_login);
                return;
            }
            usuario = presenter.onObtemUsuario(new String[]{"usuaLogin",login});
            if (usuario == null) {
                onError(R.string.erro_text_usuario);
                return;
            }

            getResultsFromApi(v);
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_cadastro_usuario)
    public void openCadastroUsuarioActivity() {
        if (presenter.onVerificaUsuarioCadastrado()) {
            Intent intent = CadastroUsuarioActivity.getStartIntent(LoginActivity.this);
            startActivity(intent);
            finish();
        } else {
            onError(R.string.erro_text_usuario_existente);
        }
    }

    @Override
    public void getStartActivity(Intent intent) {
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
        presenter.onCreateGoogleLogin();
        presenter.onCreateFacebookLogin();

        // Inicializa Internet Checker
        internetDetector = new InternetDetector(getApplicationContext());
        //Inicializa credenciais e objeto de serviço.
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), SCOPES).setBackOff(new ExponentialBackOff());

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.app_name);
        //alertDialogBuilder.setMessage("Senha enviada para o e-mail: "+usuario.getUsuaEmail()+" \nVerifique sua caixa de entrada de e-mails.");
        alertDialogBuilder.setNegativeButton("Ok", null);
        alertDialogBuilder.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getHandleActivityResult(requestCode, resultCode, data);
        presenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);

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
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
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
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    fileName = getPathFromURI(imageUri);
                    // edtAttachmentData.setText(fileName);
                }
        }
    }

    private void showMessage(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        onError(message);
    }

    private void getResultsFromApi(View view) {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (googleAccountCredential.getSelectedAccountName() == null) {
            chooseAccount(view);
        } else if (!internetDetector.checkMobileInternetConn()) {
            showMessage(view, "Sem conexão com à Internet!");
        } else {
            new MakeRequestTask(this, googleAccountCredential).execute();
        }
    }

    // Método para verificar o Google Play Service está disponível
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // Método para mostrar informações, se o Google Play Service não estiver disponível.
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    // Método para informações de erro do Google Play Service
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                LoginActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    // Armazenando o ID do Mail usando Shared Preferences
    private void chooseAccount(View view) {
        if (hasPermissoes(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                googleAccountCredential.setSelectedAccountName(accountName);
                getResultsFromApi(view);
            } else {
                // Inicie uma caixa de diálogo onde o usuário pode escolher uma conta
                startActivityForResult(googleAccountCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                chooseAccount(floatingActionButton);
                break;
            case SELECT_PHOTO:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, "", null, "");
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    // Async Task para enviar e-mails usando o OAuth do GMail
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private Gmail service = null;
        private Exception lastError = null;
        private View view = floatingActionButton;
        private LoginActivity activity;

        MakeRequestTask(LoginActivity activity, GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            service = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                lastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException {
            // Obtendo valores para, email, assunto e mensagem
            String user = "me"; //"me";
            String to = usuario.getUsuaEmail();//Utils.getString(edtToAddress);
            String from = googleAccountCredential.getSelectedAccountName();
            String subject = "Senha do app";//Utils.getString(edtSubject);
            String body =  "Prezado(a) "+usuario.getUsuaNome()+"\n\nSegue a senha solicitada: "+usuario.getUsuaSenha()+"\n\n© "+ Calendar.getInstance().get(Calendar.YEAR)+" "+getResources().getString(R.string.app_name)+"\n"+getResources().getString(R.string.app_slogan);//Utils.getString(edtMessage);
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

        // Method to send email
        private String sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
            Message message = createMessageWithEmail(email);
            // Método oficial do GMail para enviar e-mail com OAuth 2.0
            message = service.users().messages().send(userId, message).execute();

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message.getId();
        }

        // Método para criar e-mails Params
        private MimeMessage createEmail(String to,
                                        String from,
                                        String subject,
                                        String bodyText) throws MessagingException {
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

            if (!(activity.fileName.equals(""))) {
                // Crie um novo objeto MimeBodyPart e defina o objeto DataHandler para este objeto
                MimeBodyPart attachmentBody = new MimeBodyPart();
                String filename = activity.fileName; // change accordingly
                DataSource source = new FileDataSource(filename);
                attachmentBody.setDataHandler(new DataHandler(source));
                attachmentBody.setFileName(filename);
                multipart.addBodyPart(attachmentBody);
            }

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

        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected void onPostExecute(String output) {
            hideLoading();
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
            hideLoading();
            if (lastError != null) {
                if (lastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) lastError)
                                    .getConnectionStatusCode());
                } else if (lastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) lastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    showMessage(view, "O seguinte erro ocorreu:\n" + lastError);
                    Log.d("Erro", lastError + "");
                }
            } else {
                showMessage(view, "Requisição cancelada.");
            }
        }
    }
}

package br.com.erivando.vacinaskids.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.util.Arrays;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.ui.login.LoginPresenter.RC_SIGN_IN;
import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

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
        if (v == lembrarSenhaTextView) {
            presenter.enviaSenhaPorEmail(v, loginEditText.getText().toString());
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
    public void getStartActivityForResult(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    @Override
    public Activity getContextActivity() {
        return LoginActivity.this;
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
        presenter.onCreateGoogleAccountCredential();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getActivityResult(requestCode, resultCode, data);
        presenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        presenter.getRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

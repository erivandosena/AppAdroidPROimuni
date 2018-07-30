package br.com.erivando.vacinaskids.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;

import java.util.Arrays;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.cadastro.usuario.CadastroUsuarioActivity;
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
           // presenter.onCreateGoogleLogin();
          //  Intent intent = Auth.GoogleSignInApi.getSignInIntent(presenter.getGoogleApiClient());
           // startActivityForResult(intent, SIGN_IN_CODE);

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

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_cadastro_usuario)
    public void openCadastroUsuarioActivity() {
        Intent intent = CadastroUsuarioActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
       // presenter.getAccessTokenTracker().stopTracking();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
        presenter.onCreateGoogleLogin();
        presenter.onCreateFacebookLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getHandleActivityResult(requestCode, resultCode, data);
        presenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }


}

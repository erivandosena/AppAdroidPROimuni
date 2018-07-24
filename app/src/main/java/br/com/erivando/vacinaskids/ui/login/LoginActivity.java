package br.com.erivando.vacinaskids.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.cadastro.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:16
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginMvpPresenter<LoginMvpView> resenter;

    @BindView(R.id.text_usua_login)
    EditText loginEditText;

    @BindView(R.id.text_usua_senha)
    EditText senhaEditText;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        resenter.onAttach(LoginActivity.this);
    }

    @OnClick(R.id.btn_login_usuario)
    void onLoginClick(View v) {
        resenter.onLoginClick(loginEditText.getText().toString(), senhaEditText.getText().toString());
    }

    @OnClick(R.id.btn_google_login)
    void onGoogleLoginClick(View v) {
        resenter.onGoogleLoginClick();
    }

    @OnClick(R.id.btn_facebook_login)
    void onFbLoginClick(View v) {
        resenter.onFacebookLoginClick();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_cadastro_usuario)
    @Override
    public void openCadastroUsuarioActivity() {
        Intent intent = CadastroUsuarioActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        resenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
    }
}

package br.com.erivando.proimuni.ui.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.util.Arrays;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.introducao.IntroducaoActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.ui.activity.login.LoginPresenter.RC_SIGN_IN;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:16
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginMvpPresenter<LoginMvpView> presenter;

    @BindView(R.id.text_usua_login)
    EditText loginEditText;

    @BindView(R.id.text_usua_senha)
    EditText senhaEditText;

    @BindView(R.id.text_senha_toggle)
    TextView textViewSenhaToggle;

    @BindView(R.id.btn_facebook_login)
    Button facebookLogin;

    @BindView(R.id.btn_google_login)
    Button googleLogin;

    @BindView(R.id.btn_cadastro_usuario)
    Button buttonCadastroUsuario;

    @BindView(R.id.tv_btn_lembrar_senha)
    TextView lembrarSenhaTextView;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        try {
            if (isNetworkConnected()) {
                if (v == googleLogin) {
                    Intent signInIntent = presenter.getGoogleSignInClient().getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        } finally {
            hideLoading();
        }
    }

    @OnClick(R.id.btn_facebook_login)
    void onFacebookLoginClick(View v) {
        showLoading();
        try {
            if (isNetworkConnected()) {
                if (v == facebookLogin) {
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                }
            }
        } finally {
            hideLoading();
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
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1)
            showLoading();
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onOpenIntroducaoActivity() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1)
            showLoading();
        Intent intent = IntroducaoActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_cadastro_usuario)
    public void openCadastroUsuarioActivity(View v) {
        if (v == buttonCadastroUsuario) {
            if (presenter.onVerificaUsuarioCadastrado()) {
                Intent intent = CadastroUsuarioActivity.getStartIntent(LoginActivity.this);
                startActivity(intent);
                finish();
            } else {
                onError(R.string.erro_text_usuario_existente);
            }
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
        textViewSenhaToggle.setVisibility(View.GONE);

        final String texto_mostra_toggle = getResources().getString(R.string.text_mostra_senha_toggle);
        final String texto_oculta_toggle = getResources().getString(R.string.text_oculta_senha_toggle);

        textViewSenhaToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (texto_mostra_toggle.equalsIgnoreCase(textViewSenhaToggle.getText().toString())) {
                    textViewSenhaToggle.setText(texto_oculta_toggle);
                    senhaEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    senhaEditText.setSelection(senhaEditText.length());
                } else {
                    textViewSenhaToggle.setText(texto_mostra_toggle);
                    senhaEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    senhaEditText.setSelection(senhaEditText.length());
                }
            }
        });

        senhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (senhaEditText.getText().length() > 0) {
                    textViewSenhaToggle.setVisibility(View.VISIBLE);
                } else {
                    textViewSenhaToggle.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

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

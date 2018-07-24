package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CadastroUsuarioActivity extends BaseActivity implements CadastroUsuarioMvpView {

    @Inject
    CadastroUsuarioMvpPresenter<CadastroUsuarioMvpView> presenter;

    @BindView(R.id.text_cad_nome)
    EditText nomeEditText;

    @BindView(R.id.text_cad_login)
    EditText loginEditText;

    @BindView(R.id.text_cad_email)
    EditText emailEditText;

    @BindView(R.id.text_cad_senha)
    EditText senhaEditText;

    @BindView(R.id.text_cad_repete_senha)
    EditText repSenhaEditText;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CadastroUsuarioActivity.class);
        return intent;
    }

    @OnClick(R.id.btn_voltar_login)
    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(CadastroUsuarioActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        setContentView(R.layout.activity_cadastro_usuario);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(CadastroUsuarioActivity.this);
    }

    @OnClick(R.id.btn_cadadastar_usuario)
    void onCadasrarClick(View v) {
        presenter.onCadasrarClick(nomeEditText.getText().toString(), loginEditText.getText().toString(), senhaEditText.getText().toString(), emailEditText.getText().toString());
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
    }

    @Override
    public void onBackPressed() {
        this.openLoginActivity();
    }
}

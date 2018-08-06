package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.login.LoginActivity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import br.com.erivando.vacinaskids.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.ui.cadastro.usuario.CadastroUsuarioPresenter.PERMISSOES;
import static br.com.erivando.vacinaskids.ui.cadastro.usuario.CadastroUsuarioPresenter.TODAS_PERMISSOES;
import static br.com.erivando.vacinaskids.util.Uteis.base64ParaBitmap;
import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;
import static br.com.erivando.vacinaskids.util.Uteis.hasPermissoes;

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

    @BindView(R.id.img_usuario_foto)
    ImageButton fotoImageButton;

    private Usuario usuario;
    private Long id;
    private Bitmap imagemBitmapFoto;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CadastroUsuarioActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        usuario = new Usuario();

        id = 0L;

        setUp();
    }

    @OnClick(R.id.btn_nav_voltar)
    @Override
    public void openLoginOuMainActivity() {
        Intent intent = new Intent();
        try {
            if (id == 0L && presenter.getTokenUsuario() == null) {
                intent = LoginActivity.getStartIntent(this);
            } else {
                intent = MainActivity.getStartIntent(this);
            }
        } finally {
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.img_usuario_foto)
    //@Override
    public void onIncluiFotoUsuario() {
        if (!hasPermissoes(this, PERMISSOES)) {
            ActivityCompat.requestPermissions(this, PERMISSOES, TODAS_PERMISSOES);
        } else {
            presenter.selecionarImagem(this);
        }
    }

    @Override
    public void getStartActivityForResult(Intent intentImagem, int requestImgCamera) {
        startActivityForResult(intentImagem, requestImgCamera);
    }

    @OnClick(R.id.btn_cadadastar_usuario)
    public void onCadasrarClick(View v) {
        presenter.onCadasrarClick(id, nomeEditText.getText().toString(), loginEditText.getText().toString(), emailEditText.getText().toString(), senhaEditText.getText().toString(), repSenhaEditText.getText().toString(), imagemBitmapFoto);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
        usuario = presenter.onUsuarioCadastrado();
        if (usuario != null) {
            id = usuario.getId();
            nomeEditText.setText(usuario.getUsuaNome());
            loginEditText.setText(usuario.getUsuaLogin());
            emailEditText.setText(usuario.getUsuaEmail());
            senhaEditText.setText(usuario.getUsuaSenha());
            if (usuario.getUsuaFoto() != null)
                fotoImageButton.setImageBitmap(base64ParaBitmap(usuario.getUsuaFoto()));
        }
    }

    @Override
    public void onBackPressed() {
        this.openLoginOuMainActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagemBitmapFoto = presenter.onActivityResult(requestCode, resultCode, data, this, fotoImageButton);
    }
}

package br.com.erivando.proimuni.ui.activity.usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.login.LoginActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.util.Uteis.PERMISSOES;
import static br.com.erivando.proimuni.util.Uteis.TODAS_PERMISSOES;
import static br.com.erivando.proimuni.util.Uteis.base64ParaBitmap;
import static br.com.erivando.proimuni.util.Uteis.hasPermissoes;
import static br.com.erivando.proimuni.util.Uteis.statusBarTransparente;

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

   // @BindView(R.id.toolbar_usuario)
   // Toolbar toolbar;

   // @BindView(R.id.collapsing_toolbar_usuario)
   // CollapsingToolbarLayout collapsingToolbar;

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

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CadastroUsuarioActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        setUnBinder(ButterKnife.bind(this));
        //toolbar.setTitle(getResources().getString(R.string.text_usuario_titulo));
       // setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      //      @Override
       //     public void onClick(View v) {
       //         openLoginOuMainActivity();
        //    }
      //  });

        //collapsingToolbar.setTitle(getResources().getString(R.string.text_usuario_titulo));

        textViewTituloToobar.setText(getResources().getString(R.string.text_usuario_titulo));

        getActivityComponent().inject(this);

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
    public void onIncluiFoto() {
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
        if (imagemBitmapFoto == null && usuario != null)
            if(usuario.getUsuaFoto() != null)
                imagemBitmapFoto = base64ParaBitmap(usuario.getUsuaFoto());
        presenter.onCadasrarClick(id, nomeEditText.getText().toString(), loginEditText.getText().toString(), emailEditText.getText().toString(), senhaEditText.getText().toString(), repSenhaEditText.getText().toString(), imagemBitmapFoto);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        //habilitaTelaCheia(this);
        //statusBarTransparente(this);
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

    @Override
    public Context getContextActivity() {
        return CadastroUsuarioActivity.this;
    }
}

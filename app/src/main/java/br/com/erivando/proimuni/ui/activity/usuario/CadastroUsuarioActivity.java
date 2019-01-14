package br.com.erivando.proimuni.ui.activity.usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.imagem.RoundedImageButton;
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
import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

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

    @BindView(R.id.text_senha_toggle)
    TextView textViewSenhaToggle;

    @BindView(R.id.text_repete_senha_toggle)
    TextView textViewRepeteSenhaToggle;

    @BindView(R.id.img_usuario_foto)
    RoundedImageButton fotoImageButton;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

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
        setUnBinder(ButterKnife.bind(this));
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
            if (usuario.getUsuaFoto() != null)
                imagemBitmapFoto = base64ParaBitmap(usuario.getUsuaFoto());
        presenter.onCadastrarClick(id, String.valueOf(nomeEditText.getText()), loginEditText.getText().toString(), emailEditText.getText().toString(), senhaEditText.getText().toString(), repSenhaEditText.getText().toString(), imagemBitmapFoto);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        textViewSenhaToggle.setVisibility(View.GONE);
        textViewRepeteSenhaToggle.setVisibility(View.GONE);

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

        textViewRepeteSenhaToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (texto_mostra_toggle.equalsIgnoreCase(textViewRepeteSenhaToggle.getText().toString())) {
                    textViewRepeteSenhaToggle.setText(texto_oculta_toggle);
                    repSenhaEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    repSenhaEditText.setSelection(repSenhaEditText.length());
                } else {
                    textViewRepeteSenhaToggle.setText(texto_mostra_toggle);
                    repSenhaEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    repSenhaEditText.setSelection(repSenhaEditText.length());
                }
            }
        });

        repSenhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (repSenhaEditText.getText().length() > 0) {
                    textViewRepeteSenhaToggle.setVisibility(View.VISIBLE);
                } else {
                    textViewRepeteSenhaToggle.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        usuario = presenter.onUsuarioCadastrado();
        if (usuario != null) {
            id = usuario.getId();
            if(usuario.getUsuaNome() != null)
                nomeEditText.setText(usuario.getUsuaNome());
            loginEditText.setText(usuario.getUsuaLogin());
            emailEditText.setText(usuario.getUsuaEmail());
            senhaEditText.setText(usuario.getUsuaSenha());
            if (usuario.getUsuaFoto() != null)
                fotoImageButton.setImageBitmap(base64ParaBitmap(usuario.getUsuaFoto()));
        }

        resizeCustomizedToobar(linearLayoutToobar);
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

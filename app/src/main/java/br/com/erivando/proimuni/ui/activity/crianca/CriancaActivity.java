package br.com.erivando.proimuni.ui.activity.crianca;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.imagem.RoundedImageButton;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioActivity;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioMvpPresenter;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioMvpView;
import br.com.erivando.proimuni.util.Uteis;
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
 * Data/Hora:   09 de Agosto de 2018 as 10:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CriancaActivity extends BaseActivity implements CriancaMvpView {

    @Inject
    CriancaMvpPresenter<CriancaMvpView> presenter;

    @Inject
    CadastroUsuarioMvpPresenter<CadastroUsuarioMvpView> presenterUsuario;

   // @BindView(R.id.toolbar_crianca)
   // Toolbar toolbar;

  //  @BindView(R.id.collapsing_toolbar_crianca)
 //   CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.nome_crianca)
    EditText nomeEditText;

    @BindView(R.id.nascimento)
    EditText nascimentoEditText;

    /*
    @BindView(R.id.responsavel)
    Spinner comboResponsavel;
    */

    @BindView(R.id.sexo)
    Spinner comboSexo;

    @BindView(R.id.img_crianca_foto)
    RoundedImageButton fotoImageButton;

    @BindView(R.id.btn_remover_crianca)
    Button buttonRemoveCrianca;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    @BindView(R.id.btn_toggle_vacina_indigenas)
    ToggleButton toggleButtonIndigena;

    private boolean toggleStatus;

    private Intent intent;
    private ArrayAdapter<String> adapterResponsavel;
    private ArrayAdapter<String> adapterSexo;
    private Crianca crianca;
    private Long id;
    private Bitmap imagemBitmapFoto;

    private List<Crianca> listaCriancas;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CriancaActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crianca);

        setUnBinder(ButterKnife.bind(this));
        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_crianca_titulo));
        */
        textViewTituloToobar.setText(getResources().getString(R.string.text_crianca_titulo));

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        crianca = new Crianca();

        intent = getIntent();

        id = 0L;

        getCrianca();

        setUp();

        nascimentoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        Calendar calendario = Calendar.getInstance();
                        calendario.set(ano, mes, dia);
                        nascimentoEditText.setText(dateFormat.format(calendario.getTime()));

                    }
                };

                Date convertedDate;
                String[] separaData;
                int dia = 0, mes = 0, ano = 0;
                try {
                    convertedDate = dateFormat.parse((nascimentoEditText.getText().toString().length() > 0) ? nascimentoEditText.getText().toString() : "01/01/2000");
                    separaData = dateFormat.format(convertedDate).split("/");
                    dia = Integer.valueOf(separaData[0]);
                    mes = Integer.valueOf(separaData[1]);
                    ano = Integer.valueOf(separaData[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(CriancaActivity.this, dpd, ano, mes-1, dia);
                datePickerDialog.show();
            }
        });
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @OnClick(R.id.img_crianca_foto)
    public void onIncluiFoto() {
        if (!hasPermissoes(this, PERMISSOES)) {
            ActivityCompat.requestPermissions(this, PERMISSOES, TODAS_PERMISSOES);
        } else {
            presenter.selecionarImagem(this);
        }
    }

    @OnClick(R.id.btn_toggle_vacina_indigenas)
    public void onIndigenasClick(View view) {
        boolean onClick = ((ToggleButton) view).isChecked();
        if(onClick){
            toggleStatus=true;
        }
        else {
            toggleStatus=false;
        }
    }

    @OnClick(R.id.btn_cadadastar_crianca)
    public void onCadasrarClick(View v) {
        if (imagemBitmapFoto == null && crianca!= null)
            if (crianca.getCriaFoto() != null)
                imagemBitmapFoto = base64ParaBitmap(crianca.getCriaFoto());
        presenter.onCadastrarClick(id, nomeEditText.getText().toString(), nascimentoEditText.getText().toString(), comboSexo.getSelectedItem().toString(), imagemBitmapFoto, toggleStatus);
    }

    @OnClick(R.id.btn_remover_crianca)
    public void onRemoverClick(View v) {
        if (crianca!= null)
            presenter.onRemoveCrianca(id);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        if (intent != null) {
            id = intent.getLongExtra("crianca", 0L);
            crianca = presenter.onCriancaCadastrada(id);
            if (crianca != null) {
                nomeEditText.setText(crianca.getCriaNome());
                nascimentoEditText.setText(Uteis.getParseDateString(crianca.getCriaNascimento()));
                //comboResponsavel.setSelection(adapterResponsavel.getPosition(crianca.getUsuario().getUsuaNome()));
                comboSexo.setSelection(adapterSexo.getPosition(crianca.getCriaSexo()));
                if (crianca.getCriaFoto() != null)
                    fotoImageButton.setImageBitmap(base64ParaBitmap(crianca.getCriaFoto()));
                buttonRemoveCrianca.setVisibility(View.VISIBLE);
                toggleButtonIndigena.setChecked(crianca.isCriaEtnia());
            } else {
                buttonRemoveCrianca.setVisibility(View.GONE);
            }
        } else {

        }

        resizeCustomizedToobar(linearLayoutToobar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!listaCriancas.isEmpty())
            openCriancaListaActivity("edita");
        else {
            openMainActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagemBitmapFoto =  presenter.onActivityResult(requestCode, resultCode, data, this, fotoImageButton);
    }

    @Override
    public void getStartActivityForResult(Intent intentImagem, int requestImgCamera) {
        startActivityForResult(intentImagem, requestImgCamera);
    }

    @Override
    public void openCriancaActivity() {

    }

    @Override
    public void openCriancaListaActivity(String acao) {
        Intent intent = CriancaListaActvity.getStartIntent(CriancaActivity.this);
        if ("edita".equals(acao))
            intent.putExtra("criancaLista", acao);
        startActivity(intent);
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(CriancaActivity.this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }


    private void getCrianca() {

        listaCriancas = presenter.onCriancaCadastrada();
        /*
        Usuario usuario = presenterUsuario.onUsuarioCadastrado();
        if (usuario != null) {
            String[] responsavel = new String[]{usuario.getUsuaNome()};
            adapterResponsavel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsavel);
            adapterResponsavel.setDropDownViewResource(android.R.layout.simple_spinner_item);
            //comboResponsavel.setAdapter(adapterResponsavel);

        } else {
         //   Toast.makeText(this, this.getString(R.string.texto_aviso_usuario_nao_cadastrado), Toast.LENGTH_LONG).show();
         //   openUsuarioActivity();
        }
        */

        String[] sexo = new String[]{"Menino", "Menina"};
        adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexo);
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_item);
        comboSexo.setAdapter(adapterSexo);
    }

    public void openUsuarioActivity() {
        Intent intent = new Intent();
        try {
            intent = CadastroUsuarioActivity.getStartIntent(this);
        } finally {
            startActivity(intent);
            finish();
        }
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public Context getContextActivity() {
        return CriancaActivity.this;
    }
}

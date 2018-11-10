package br.com.erivando.vacinaskids.ui.activity.crianca;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioMvpView;
import br.com.erivando.vacinaskids.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

import static br.com.erivando.vacinaskids.util.Uteis.PERMISSOES;
import static br.com.erivando.vacinaskids.util.Uteis.TODAS_PERMISSOES;
import static br.com.erivando.vacinaskids.util.Uteis.base64ParaBitmap;
import static br.com.erivando.vacinaskids.util.Uteis.getCurrentTimeStamp;
import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;
import static br.com.erivando.vacinaskids.util.Uteis.hasPermissoes;

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

    @BindView(R.id.toolbar_crianca)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_crianca)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.text_cad_nome)
    EditText nomeEditText;

    @BindView(R.id.text_nascimento)
    EditText nascimentoEditText;

    @BindView(R.id.responsavel)
    Spinner comboResponsavel;

    @BindView(R.id.sexo)
    Spinner comboSexo;

    @BindView(R.id.img_crianca_foto)
    ImageButton fotoImageButton;

    private Intent intent;
    private ArrayAdapter<String> adapterResponsavel;
    private ArrayAdapter<String> adapterSexo;
    private Crianca crianca;
    private Long id;
    private Bitmap imagemBitmapFoto;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CriancaActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crianca);

        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_crianca_titulo));

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

    @OnClick(R.id.img_crianca_foto)
    public void onIncluiFoto() {
        if (!hasPermissoes(this, PERMISSOES)) {
            ActivityCompat.requestPermissions(this, PERMISSOES, TODAS_PERMISSOES);
        } else {
            presenter.selecionarImagem(this);
        }
    }

    @OnClick(R.id.btn_cadadastar_crianca)
    public void onCadasrarClick(View v) {
        if (imagemBitmapFoto == null && crianca!= null)
            if (crianca.getCriaFoto() != null)
                imagemBitmapFoto = base64ParaBitmap(crianca.getCriaFoto());
        presenter.onCadasrarClick(id, nomeEditText.getText().toString(), nascimentoEditText.getText().toString(), comboResponsavel.getSelectedItem().toString(), comboSexo.getSelectedItem().toString(), imagemBitmapFoto);
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
                comboResponsavel.setSelection(adapterResponsavel.getPosition(crianca.getUsuario().getUsuaNome()));
                comboSexo.setSelection(adapterSexo.getPosition(crianca.getCriaSexo()));
                if (crianca.getCriaFoto() != null)
                    fotoImageButton.setImageBitmap(base64ParaBitmap(crianca.getCriaFoto()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCriancaListaActivity("edita");
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
        startActivity(CriancaListaActvity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCriancaListaActivity(String acao) {
        Intent intent = CriancaListaActvity.getStartIntent(CriancaActivity.this);
        if ("edita".equals(acao))
            intent.putExtra("criancaLista", acao);
        startActivity(intent);
        finish();
    }

    private void getCrianca() {
        Usuario usuario = presenterUsuario.onUsuarioCadastrado();
        if (usuario != null) {
            String[] responsavel = new String[]{usuario.getUsuaNome()};
            adapterResponsavel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsavel);
            adapterResponsavel.setDropDownViewResource(android.R.layout.simple_spinner_item);
            comboResponsavel.setAdapter(adapterResponsavel);

            String[] sexo = new String[]{"Menino", "Menina"};
            adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexo);
            adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_item);
            comboSexo.setAdapter(adapterSexo);
        } else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_usuario_nao_cadastrado), Toast.LENGTH_LONG).show();
            openUsuarioActivity();
        }
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

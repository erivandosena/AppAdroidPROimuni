package br.com.erivando.proimuni.ui.activity.cartao;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.imagem.RoundedImageView;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioMvpPresenter;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioMvpView;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpView;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaActivity;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpView;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.proimuni.ui.adapter.CartaoPdfIdadeRVA;
import br.com.erivando.proimuni.ui.adapter.CartaoPdfRVA;
import br.com.erivando.proimuni.ui.adapter.VacinaRVA;
import br.com.erivando.proimuni.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

import static br.com.erivando.proimuni.util.Uteis.checkStoragePermissions;
import static br.com.erivando.proimuni.util.Uteis.getParseDateString;
import static br.com.erivando.proimuni.util.Uteis.isExternalStorage;
import static br.com.erivando.proimuni.util.Uteis.obtemIdadeCompleta;
import static br.com.erivando.proimuni.util.Uteis.obtemIdadePorDiaOuMesOuAno;
import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   19 de Outubro de 2018 as 20:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoDetalheActivity extends BaseActivity implements CartaoMvpView {

    private File EXPORT_PDF_EXTERNAL_PATH;
    private File EXPORT_PDF_INTERNAL_PATH;
    private File EXPORT_PDF_PATH;
    private String EXPORT_PDF_FILE_NAME;
    private File PDF_FILE;

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    @Inject
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @Inject
    CalendarioMvpPresenter<CalendarioMvpView> calendarioPresenter;

    @Inject
    ImunizacaoMvpPresenter<ImunizacaoMvpView> imunizacaoPresenter;

    @Inject
    ConfiguracaoMvpPresenter<ConfiguracaoMvpView> configuracaoPresenter;

    @BindView(R.id.fab_cartao_print)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.text_titulo_crianca_cartao)
    public TextView mTextCrianca;

    @BindView(R.id.text_sub_titulo_crianca_cartao)
    public TextView mTextIdade;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.image_crianca_cartao)
    RoundedImageView roundedImageViewCrianca;

    @BindView(R.id.card_view_cartao)
    public CardView cardViewCartaoCrianca;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    @BindView(R.id.text_pesquisa_vacina)
    TextInputEditText textInputPesquisa;

    @BindView(R.id.btn_pesquisa_vacina)
    ImageButton imageButtonPesquisa;

    private List<Calendario> calendarioList;
    private List<Imunizacao> listaImunizacoes;
    private VacinaRVA adapter;
    private Cartao cartao;
    private Intent intent;
    private Long idCartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoDetalheActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_detalhe);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        textViewTituloToobar.setText(getResources().getString(R.string.menu_cartao));

        intent = getIntent();

        setUp();

        textInputPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    hideKeyboard();
                    montaCartao();
                }
            }
        });
    }

    @OnClick(R.id.btn_pesquisa_vacina)
    public void onClickPesquisa(View view) {
        if (view == imageButtonPesquisa) {
            hideKeyboard();
            showLoading();
            if (textInputPesquisa.getText().length() > 0) {
                calendarioList = calendarioPresenter.onCalendariosPorNomeVacina(new String[]{"vacina.vaciNome", textInputPesquisa.getText().toString().trim()});
                if (calendarioList.isEmpty()) {
                    calendarioList = calendarioPresenter.onCalendariosPorVacina(new String[]{"vacina.vaciNome", textInputPesquisa.getText().toString().trim()});
                    if (calendarioList.isEmpty()) {
                        textInputPesquisa.setText("");
                        Toast.makeText(CartaoDetalheActivity.this, R.string.aviso_vacina_nao_encontrada, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            montaCartao();
            hideLoading();
        }
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @OnClick(R.id.card_view_cartao)
    public void onClickCartao(View v) {
        if (cartao != null) {
            Intent intent = CriancaActivity.getStartIntent(this);
            intent.putExtra("crianca", cartao.getCrianca().getId());
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.fab_cartao_print)
    public void onClick(View view) {
        if (view == fabFloatingActionButton) {
            if(!listaImunizacoes.isEmpty()) {
                criaPDF(CartaoDetalheActivity.this);
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(R.string.text_tit_cartao_pdf)
                        .setMessage("\nNão existe vacina com dose cadastrada.\n")
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    return;
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }

    @Override
    protected void setUp() {
        if (intent != null) {
            idCartao = intent.getLongExtra("cartao", 0L);
            cartao = presenter.onCartaoCadastrado(idCartao);
            if (cartao == null) {
                startActivity(CartaoListaActvity.getStartIntent(this).putExtra("cartaoLista", "cartao"));
                finish();
            } else {
                if (cartao.getCrianca().getCriaFoto() != null)
                    roundedImageViewCrianca.setImageBitmap(Uteis.base64ParaBitmap(cartao.getCrianca().getCriaFoto()));
                mTextCrianca.setText(cartao.getCrianca().getCriaNome());
                mTextIdade.setText(obtemIdadeCompleta(cartao.getCrianca().getCriaNascimento()));
            }
        }

        montaCartao();
    }

    private void montaCartao() {
        List<Idade> idadeList = idadePresenter.onIdadesCadastradas();

        if (textInputPesquisa.getText().length() == 0)
            calendarioList = calendarioPresenter.onCalendariosCadastrados();

        List<Vacina> listaVacinas = new ArrayList<Vacina>();
        List<Dose> listaDoses = new ArrayList<Dose>();
        List<Idade> listaIdades = new ArrayList<Idade>();
        listaImunizacoes = new ArrayList<Imunizacao>();
        List<Imunizacao> listaImunizacoesHPV = new ArrayList<Imunizacao>();
        for (Idade idade : idadeList) {
            for (Calendario calendarioItem : calendarioList) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    listaVacinas.add(calendarioItem.getVacina());
                    listaDoses.add(calendarioItem.getDose());
                    listaIdades.add(calendarioItem.getIdade());
                    Imunizacao imunizacao = imunizacaoPresenter.onImunizacaoCadastrada(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), idCartao});
                    if (imunizacao != null) {
                        listaImunizacoes.add(imunizacao);
                        if ("HPV".equalsIgnoreCase(imunizacao.getVacina().getVaciNome()) && listaImunizacoesHPV.isEmpty())
                            listaImunizacoesHPV = imunizacaoPresenter.onImunizacoesCadastradas(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), idCartao});
                    }
                }
            }
        }

        RecyclerView recycler_view = findViewById(R.id.cartao_lista_vacina_recyclerView);
        adapter = new VacinaRVA(listaVacinas, listaDoses, listaIdades, listaImunizacoes, cartao, configuracaoPresenter.onRedeVacinas(), listaImunizacoesHPV, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setHasFixedSize(false);
        recycler_view.setAdapter(adapter);

        cardViewCartaoCrianca.setCardBackgroundColor(getResources().getColor((R.color.colorPurpleLight)));
        cardViewCartaoCrianca.setRadius(20f);
        cardViewCartaoCrianca.setCardElevation(2f);
        cardViewCartaoCrianca.setUseCompatPadding(true);

        resizeCustomizedToobar(linearLayoutToobar);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCartaoListaActivity("cartao");
    }

    @Override
    public Context getContextActivity() {
        return CartaoDetalheActivity.this;
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    private void criaPDF(final Context context) {
        AbstractViewRenderer pagina = new AbstractViewRenderer(context, R.layout.activity_cartao_vacinas) {
            private List<Idade> idadesRemovidas;
            private RecyclerView mRecyclerViewIdades;
            private RecyclerView mRecyclerViewCalendario;
            private CartaoPdfIdadeRVA mCartaoPdfIdadesAdapter;
            private List<Idade> todasIdades;
            private List<Idade> idades;
            private CartaoPdfRVA mCartaoPdfAdapter;
            private List<Calendario> calendarios;
            private List<Calendario> calendarioVacinasDoses;
            private List<Imunizacao> imunizacoesHPV;
            private TextView nomeCrianca;
            private TextView idadeCrianca;
            private TextView etniaCrianca;
            private TextView redeVacinas;
            private TextView textDataGerado;
            private TextView textCopyright;

            private void criaDadosCalendario() {
                idadesRemovidas = new ArrayList<Idade>();
                idades = new ArrayList<Idade>();
                calendarioVacinasDoses = new ArrayList<Calendario>();
                imunizacoesHPV = new ArrayList<Imunizacao>();
                todasIdades = idadePresenter.onIdadesCadastradas();
                calendarios = calendarioPresenter.onCalendariosCadastrados();

                for (Idade idade : todasIdades) {
                    idades.add(idade);

                    if ("Menina".equalsIgnoreCase(cartao.getCrianca().getCriaSexo()))
                        if ("11 a 14 Anos".equalsIgnoreCase(idade.getIdadDescricao())) {
                            idades.remove(idade);
                        }
                    if ("Menino".equalsIgnoreCase(cartao.getCrianca().getCriaSexo()))
                        if ("9 a 14 Anos".equalsIgnoreCase(idade.getIdadDescricao())) {
                            idades.remove(idade);
                        }
                }

                for (Idade idade : idades) {
                    Calendario calendario = new Calendario();
                    RealmList<Vacina> itemVacina = new RealmList<Vacina>();
                    RealmList<Dose> itemDose = new RealmList<Dose>();
                    RealmList<Idade> itemIdade = new RealmList<Idade>();
                    RealmList<Imunizacao> itemImunizacao = new RealmList<Imunizacao>();

                    for (Calendario itemCalendario : calendarios) {

                        if (itemCalendario.getIdade().getId() == idade.getId()) {
                            itemVacina.add(itemCalendario.getVacina());
                            itemDose.add(itemCalendario.getDose());

                            if("Pneumocócica 23V".equalsIgnoreCase(itemCalendario.getVacina().getVaciNome()))
                                if(!cartao.getCrianca().isCriaEtnia()) {
                                    itemVacina.remove(itemCalendario.getVacina());
                                    itemDose.remove(itemCalendario.getDose());
                                }


                            if("Privada".equalsIgnoreCase(itemCalendario.getVacina().getVaciRede())) {
                                if (!configuracaoPresenter.onRedeVacinas()) {
                                    itemVacina.remove(itemCalendario.getVacina());
                                    itemDose.remove(itemCalendario.getDose());
                                }
                            }

                            Imunizacao imunizacao = imunizacaoPresenter.onImunizacaoCadastrada(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{itemCalendario.getVacina().getId(), itemCalendario.getDose().getId(), idCartao});
                            if(imunizacao != null) {
                                itemImunizacao.add(imunizacao);
                                if ("HPV".equalsIgnoreCase(imunizacao.getVacina().getVaciNome()) && imunizacoesHPV.isEmpty())
                                    imunizacoesHPV = imunizacaoPresenter.onImunizacoesCadastradas(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{itemCalendario.getVacina().getId(), itemCalendario.getDose().getId(), idCartao});
                            }

                        }
                    }

                    calendario.setVacinasInSection(itemVacina);
                    calendario.setDosesInSection(itemDose);
                    calendario.setIdadesInSection(itemIdade);
                    calendario.setImunizacoesInSection(itemImunizacao);
                    calendarioVacinasDoses.add(calendario);

                    if(calendario.getVacinasInSection().size() == 0)
                        if(!idadesRemovidas.contains(idade))
                            idadesRemovidas.add(idade);

                }

                idades.removeAll(idadesRemovidas);

                EXPORT_PDF_EXTERNAL_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                EXPORT_PDF_INTERNAL_PATH = new File(context.getFilesDir() + File.separator + Environment.DIRECTORY_DOWNLOADS);
                EXPORT_PDF_FILE_NAME = "CartaoVacinal-"+cartao.getCrianca().getCriaNome().replace(" ", "")+".pdf";

                if (isExternalStorage()) {
                    if (checkStoragePermissions(context) == 0) {
                        EXPORT_PDF_EXTERNAL_PATH.mkdirs();
                    }
                    EXPORT_PDF_PATH = EXPORT_PDF_EXTERNAL_PATH;
                } else {
                    EXPORT_PDF_INTERNAL_PATH.mkdirs();
                    EXPORT_PDF_PATH = EXPORT_PDF_INTERNAL_PATH;
                }

                PDF_FILE = new File(EXPORT_PDF_PATH, EXPORT_PDF_FILE_NAME);

                if (PDF_FILE.exists())
                    PDF_FILE.delete();
            }

            @Override
            protected void initView(View view) {
                criaDadosCalendario();

                nomeCrianca = (TextView) view.findViewById(R.id.text_pdf_nome_crianca);
                idadeCrianca = (TextView) view.findViewById(R.id.text_pdf_idade_crianca);
                etniaCrianca = (TextView) view.findViewById(R.id.text_pdf_etnia_crianca);
                redeVacinas = (TextView) view.findViewById(R.id.text_pdf_rede_vacinas);
                textDataGerado = (TextView) view.findViewById(R.id.text_pdf_data);
                textCopyright = (TextView) view.findViewById(R.id.text_pdf_copyright);

                mRecyclerViewIdades = (RecyclerView) view.findViewById(R.id.recycler_view_pdf_idades);
                mRecyclerViewCalendario = (RecyclerView) view.findViewById(R.id.recycler_view_pdf_vacinas);

                nomeCrianca.setText("Nome: "+cartao.getCrianca().getCriaNome().toUpperCase());
                idadeCrianca.setText("Idade: "+obtemIdadePorDiaOuMesOuAno(cartao.getCrianca().getCriaNascimento()).toUpperCase());
                if (cartao.getCrianca().isCriaEtnia())
                    etniaCrianca.setText("Criança: Indígena");
                else
                    etniaCrianca.setVisibility(View.GONE);
                redeVacinas.setText("Vacinas da Rede: "+ (configuracaoPresenter.onRedeVacinas() ? "PÚBLICA/PRIVADA" : "PÚBLICA"));
                textDataGerado.setText("Gerado em " + getParseDateString(Calendar.getInstance().getTime()));
                textCopyright.setText("© " + Calendar.getInstance().get(Calendar.YEAR) + " " + context.getResources().getString(R.string.app_name));

                mRecyclerViewIdades.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                mCartaoPdfIdadesAdapter = new CartaoPdfIdadeRVA(idades);
                mRecyclerViewIdades.setAdapter(mCartaoPdfIdadesAdapter);
                mRecyclerViewIdades.setHasFixedSize(false);

                mRecyclerViewCalendario.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                mCartaoPdfAdapter = new CartaoPdfRVA(calendarioVacinasDoses, imunizacoesHPV, context);
                mRecyclerViewCalendario.setAdapter(mCartaoPdfAdapter);
                mRecyclerViewCalendario.setHasFixedSize(true);
            }

        };

        pagina.setReuseBitmap(false);
        PdfDocument documento = new PdfDocument(context);
        // adicionar quantas páginas forem necessárias
        documento.addPage(pagina);
        documento.setRenderWidth(8268);
        documento.setRenderHeight(11693);
        documento.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        documento.setProgressTitle(R.string.app_name);
        documento.setProgressMessage(R.string.gera_pdf);
        documento.setFileName(EXPORT_PDF_FILE_NAME.replace(".pdf", ""));
        documento.setSaveDirectory(EXPORT_PDF_PATH); //context.getExternalFilesDir(null));
        documento.setInflateOnMainThread(false);
        documento.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                new AlertDialog.Builder(CartaoDetalheActivity.this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(getResources().getString(R.string.text_cartao_titulo))
                        .setMessage("Cartão em formato PDF gerado com sucesso!")
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exibePDF(PDF_FILE);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        Toast.makeText(context, "Gerando PDF em:\n" + EXPORT_PDF_PATH + File.separator + EXPORT_PDF_FILE_NAME, Toast.LENGTH_LONG).show();
        documento.createPdf(context);
    }

    @Override
    public String toString() {
        return "CartaoDetalheActivity{}";
    }

    public void exibePDF(File arquivoPdf) {
        if (arquivoPdf.exists()) {
            Uri path = Uri.fromFile(arquivoPdf);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(path, "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Abrir arquivo");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruir instalar um leitor de PDF
                Toast.makeText(getApplicationContext(), "Instale um leitor de PDF!", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getApplicationContext(), "Não foi possível exibir o documento.", Toast.LENGTH_LONG).show();
    }
}

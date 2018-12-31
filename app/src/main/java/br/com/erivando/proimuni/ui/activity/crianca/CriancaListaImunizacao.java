package br.com.erivando.proimuni.ui.activity.crianca;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoMvpView;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.proimuni.ui.adapter.CriancaImunizacoesAdapter;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   30 de Dezembro de 2018 as 12:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CriancaListaImunizacao extends BaseActivity implements CriancaMvpView {

    @Inject
    ImunizacaoMvpPresenter<ImunizacaoMvpView> presenter;

    @Inject
    CartaoMvpPresenter<CartaoMvpView> cartaoPresenter;

    @Inject
    CriancaMvpPresenter<CriancaMvpView> criancaPresenter;

    @BindView(R.id.lista_imunizacoes)
    ListView listViewImunizacoes;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    private List<Imunizacao> listaImunizacoes;
    private Cartao cartao;
    private Intent intent;
    private Long idCartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CriancaListaImunizacao.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunizacoes);
        setUnBinder(ButterKnife.bind(this));
        textViewTituloToobar.setText(getResources().getString(R.string.text_imunizacoes_titulo));
        getActivityComponent().inject(this);
        criancaPresenter.onAttach(this);
        intent = getIntent();
        idCartao = 0L;
        setUp();
    }

    @Override
    protected void setUp() {
        if (intent != null) {
            idCartao = intent.getLongExtra("cartao", 0L);
            cartao = cartaoPresenter.onCartaoCadastrado(idCartao);
            if(cartao != null) {
                listaImunizacoes = presenter.onImunizacoesCartaoCrianca(new String[]{"cartao.id"}, new Long[]{idCartao});
                if(!listaImunizacoes.isEmpty()) {
                    final CriancaImunizacoesAdapter adapterImunizacao = new CriancaImunizacoesAdapter(this, listaImunizacoes);
                    listViewImunizacoes.setAdapter(adapterImunizacao);
                    listViewImunizacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> a, View v, final int position, long id) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CriancaListaImunizacao.this);
                            alertDialog.setIcon(R.drawable.ic_launcher_round);
                            alertDialog.setTitle(getResources().getString(R.string.text_titulo_remove_imunizacao));
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("A imunização da "+listaImunizacoes.get(position).getDose()+" de "+listaImunizacoes.get(position).getVacina().getVaciNome()+" será removida!\n\nConfirme Sim para continuar ou Não para cancelar.");
                            alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AppAplicacao.contextApp, AppAplicacao.contextApp.getString(R.string.texto_aviso_remove_imunizacao), Toast.LENGTH_LONG).show();
                                    if (presenter.onRemoveImunizacao(listaImunizacoes.get(position).getId())) {
                                        adapterImunizacao.notifyDataSetChanged();
                                        listViewImunizacoes.refreshDrawableState();
                                       // Intent intent = CriancaListaImunizacao.getStartIntent(CriancaListaImunizacao.this);
                                       // intent.putExtra("criancaLista", "edita");
                                       // startActivity(intent);
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.show();
                        }
                    });
                }
                else {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.ic_launcher_round)
                            .setTitle(AppAplicacao.contextApp.getResources().getString(R.string.text_imunizacao_titulo))
                            .setMessage("\nNão foi registrada nenhuma imunização!\n")
                            .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Intent intent = CriancaListaActvity.getStartIntent(CriancaListaImunizacao.this);
                                        intent.putExtra("criancaLista", "edita");
                                        startActivity(intent);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            } else {

            }
        }
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    public void onDestroy() {
        criancaPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCriancaListaActivity("edita");
    }

    @Override
    public void getStartActivityForResult(Intent intentImagem, int requestImgCamera) {

    }

    @Override
    public void openCriancaActivity() {

    }

    @Override
    public void openCriancaListaActivity(String acao) {
        startActivity(CriancaListaActvity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {

    }

    @Override
    public Context getContextActivity() {
        return null;
    }
}

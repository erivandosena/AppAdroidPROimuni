package br.com.erivando.vacinaskids.ui.activity.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpView;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpView;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoActivity;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.adapter.VacinaRVA;
import br.com.erivando.vacinaskids.ui.fragment.vacina.VacinaRecyclerViewAdapter;
import br.com.erivando.vacinaskids.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   19 de Outubro de 2018 as 20:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoDetalheActivity extends BaseActivity implements CartaoMvpView{

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @Inject
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    @Inject
    DoseMvpPresenter<DoseMvpView> dosePresenter;

    @Inject
    CalendarioMvpPresenter<CalendarioMvpView> calendarioPresenter;

    @BindView(R.id.image_cartao_vacinal)
    public ImageView mImagemCartao;

    @BindView(R.id.text_nome_crianca)
    public TextView mTextCrianca;

    @BindView(R.id.text_idade_crianca)
    public TextView mTextIdade;

    private Intent intent;

    private Cartao cartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoDetalheActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_detalhe);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //executar o que quiser no clique da seta esquerda
                onBackPressed();
            }
        });
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getResources().getString(R.string.menu_item1));

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        intent = getIntent();

        setUp();
    }

    @Override
    protected void setUp() {
        if(intent != null) {
            Long idCartao = intent.getLongExtra("cartao", -1L);
            cartao = presenter.onCartaoCadastrado(idCartao);
            if (cartao.getCrianca().getCriaFoto() != null)
                mImagemCartao.setImageBitmap(Uteis.base64ParaBitmap(cartao.getCrianca().getCriaFoto()));
            mTextCrianca.setText(cartao.getCrianca().getCriaNome());
            mTextIdade.setText("Idade: "+ Uteis.obtemIdadeCompleta(cartao.getCrianca().getCriaNascimento()));
        }

        List<Idade> idadeList = idadePresenter.onIdadesCadastradas();
        List<Calendario> calendarioList = calendarioPresenter.onCalendariosCadastrados();

        List<Vacina> listaVacinas = new ArrayList<Vacina>();
        List<Dose> listaDoses = new ArrayList<Dose>();
        List<Idade> listaIdades = new ArrayList<Idade>();
        for (Idade idade : idadeList) {
            RealmList<Vacina> vacinaItem = new RealmList<Vacina>();
            for (Calendario calendarioItem : calendarioList) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    listaVacinas.add(calendarioItem.getVacina());
                    listaDoses.add(calendarioItem.getDose());
                    listaIdades.add(calendarioItem.getIdade());
                }
            }
        }

        RecyclerView my_recycler_view = findViewById(R.id.cartao_lista_vacina_recyclerView);
        my_recycler_view.setHasFixedSize(true);
        VacinaRVA adapter = new VacinaRVA(listaVacinas, listaDoses, listaIdades, cartao, this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
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

    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    public void openImunizacaoActivity() {
        startActivity(ImunizacaoActivity.getStartIntent(this));
        finish();
    }

}
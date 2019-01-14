package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Janeiro de 2019 as 01:26
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoPdfRVA extends RecyclerView.Adapter<CartaoPdfRVA.ItemRowHolder> {

    private List<Calendario> calendarioList;
    private List<Imunizacao> imunizacaoHpvList;
    private Context mContext;
    private RecyclerView rvListaIdades;

    public CartaoPdfRVA(List<Calendario> calendarioList, List<Imunizacao> imunizacaoHpvList, Context mContext) {
        this.calendarioList = calendarioList;
        this.imunizacaoHpvList = imunizacaoHpvList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View vIdades = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cartao_pdf_idades_lista, null);
        this.rvListaIdades = (RecyclerView) vIdades.findViewById(R.id.recycler_view_pdf_idades_lista);
        View vVacinas = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cartao_pdf_vacinas_lista, null);
        ItemRowHolder mh = new ItemRowHolder(vVacinas);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        RealmList<Idade> idadeItems = calendarioList.get(i).getIdadesInSection();
        RealmList<Vacina> vacinaItems = calendarioList.get(i).getVacinasInSection();
        RealmList<Dose> doseItems = calendarioList.get(i).getDosesInSection();
        RealmList<Imunizacao> imunizacaoItems = calendarioList.get(i).getImunizacoesInSection();

        CartaoPdfIdadeRVA itemListIdadeDataAdapter = new CartaoPdfIdadeRVA(idadeItems);
        rvListaIdades.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvListaIdades.setAdapter(itemListIdadeDataAdapter);
        rvListaIdades.setHasFixedSize(true);
        //rvListaIdades.setNestedScrollingEnabled(false);

        CartaoPdfVacinaRVA itemListaVacinaDataAdapter = new CartaoPdfVacinaRVA(vacinaItems, doseItems, imunizacaoItems, imunizacaoHpvList, mContext);
        itemRowHolder.rvListaVacinas.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        itemRowHolder.rvListaVacinas.setAdapter(itemListaVacinaDataAdapter);
        itemRowHolder.rvListaVacinas.setHasFixedSize(true);
        //itemRowHolder.rvListaVacinas.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return (null != calendarioList ? calendarioList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_view_pdf_vacinas_lista)
        protected RecyclerView rvListaVacinas;

        public ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
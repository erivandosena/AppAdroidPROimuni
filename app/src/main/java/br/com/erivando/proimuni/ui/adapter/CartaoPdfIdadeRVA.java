package br.com.erivando.proimuni.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Idade;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Janeiro de 2019 as 21:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoPdfIdadeRVA extends RecyclerView.Adapter<CartaoPdfIdadeRVA.ItemRowHolder> {

    //private final List<Idade> mIdades;
    private List<Idade> mIdades;

    public CartaoPdfIdadeRVA(List<Idade> lista) {
        mIdades = lista;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartao_pdf_idades, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        holder.textTituloIdade.setText(mIdades.get(position).getIdadDescricao());
    }

    @Override
    public int getItemCount() {
        return mIdades != null ? mIdades.size() : 0;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pdf_text_titulo_idade)
        public TextView textTituloIdade;

        public ItemRowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
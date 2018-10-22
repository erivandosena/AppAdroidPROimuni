package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:15
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaRVAdapter extends RecyclerView.Adapter<VacinaRVAdapter.SingleItemRowHolder> {

    private RealmList<Vacina> vacinaList;
    private RealmList<Dose> doseList;
    private RealmList<Idade> idadeList;
    private Context mContext;

    public VacinaRVAdapter(RealmList<Vacina> vacinaList, RealmList<Dose> doseList, RealmList<Idade> idadeList, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.mContext = mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_calendario_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        Vacina singleItemVacina = vacinaList.get(i);
        Dose singleItemDose = doseList.get(i);
        Idade singleItemIdade = idadeList.get(i);

        String rede = new String();
        if("Pública".equals(singleItemVacina.getVaciRede()))
            rede = "Disponível na rede";
        else
            rede = "Opcional na rede";

        holder.textTituloVacina.setText(singleItemVacina.getVaciNome());
        holder.textTituloDose.setText(singleItemDose.getDoseDescricao());
        holder.textTituloRede.setText(rede + " "+singleItemVacina.getVaciRede());
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView imageItem;
        protected TextView textTituloVacina;
        protected TextView textTituloDose;
        protected TextView textTituloRede;

        public SingleItemRowHolder(View view) {
            super(view);
            this.imageItem = view.findViewById(R.id.image_item);
            this.textTituloVacina = view.findViewById(R.id.texto_titulo_vacina);
            this.textTituloDose = view.findViewById(R.id.texto_titulo_dose);
            this.textTituloRede = view.findViewById(R.id.texto_titulo_rede);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), textTituloVacina.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}

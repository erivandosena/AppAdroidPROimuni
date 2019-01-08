package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaDetalheActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:15
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CalendarioVacinaRVA extends RecyclerView.Adapter<CalendarioVacinaRVA.SingleItemRowHolder> {

    private RealmList<Vacina> vacinaList;
    private RealmList<Dose> doseList;
    private RealmList<Idade> idadeList;

    Context context;

    public CalendarioVacinaRVA(RealmList<Vacina> vacinaList, RealmList<Dose> doseList, RealmList<Idade> idadeList, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.context = mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_calendario_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {
        Vacina singleItemVacina = vacinaList.get(i);
        Dose singleItemDose = doseList.get(i);
        Idade singleItemIdade = idadeList.get(i);

        String rede = new String();
        if ("Pública".equals(singleItemVacina.getVaciRede()))
            rede = "Disponível na rede";
        else
            rede = "Opcional na rede";

        String genero = new String();
        if("9 a 14 anos".equalsIgnoreCase(singleItemIdade.getIdadDescricao()))
            genero = "(Meninas)";

        if("11 a 14 anos".equalsIgnoreCase(singleItemIdade.getIdadDescricao()))
            genero = "(Meninos)";

        holder.textTituloVacina.setText(singleItemVacina.getVaciNome());
        holder.textTituloDose.setText(singleItemDose.getDoseDescricao());
        holder.textTituloRede.setText(rede + " " + singleItemVacina.getVaciRede() + ("Pneumocócica 23V".equalsIgnoreCase(singleItemVacina.getVaciNome()) ? "\n(Crianças indígenas)" : "") + ("HPV".equalsIgnoreCase(singleItemVacina.getVaciNome()) ? "\n"+genero : ""));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, VacinaDetalheActivity.class);
                intent.putExtra(VacinaDetalheActivity.EXTRA_NOME, vacinaList.get(i).getVaciNome());
                intent.putExtra(VacinaDetalheActivity.EXTRA_REDE, vacinaList.get(i).getVaciRede());
                intent.putExtra(VacinaDetalheActivity.EXTRA_DESC, vacinaList.get(i).getVaciDescricao());
                intent.putExtra(VacinaDetalheActivity.EXTRA_ADMIN, vacinaList.get(i).getVaciAdministracao());
                intent.putExtra("Activity", "Calendário");
                context.startActivity(intent);
            }
        });

        holder.cardViewColorCalendarioVacina.setCardBackgroundColor(context.getResources().getColor((R.color.colorBlue)));
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

        @BindView(R.id.cardview_calendario_vacina)
        protected CardView cardViewColorCalendarioVacina;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.imageItem = view.findViewById(R.id.image_item);
            this.textTituloVacina = view.findViewById(R.id.texto_titulo_vacina);
            this.textTituloDose = view.findViewById(R.id.texto_titulo_dose);
            this.textTituloRede = view.findViewById(R.id.texto_titulo_rede);

            /*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(v.getContextActivity(), textTituloVacina.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            */

        }

    }

}

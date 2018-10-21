package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoActivity;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   20 de Outubro de 2018 as 00:54
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class VacinaRVA extends RecyclerView.Adapter<VacinaRVA.SingleItemRowHolder> {

    private List<Vacina> vacinaList;
    private List<Dose> doseList;
    private List<Idade> idadeList;
    private Cartao cartaoVacinal;

    private Context mContext;

    public VacinaRVA(List<Vacina> vacinaList, List<Dose> doseList, List<Idade> idadeList, Cartao cartaoVacinal, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.cartaoVacinal = cartaoVacinal;
        this.mContext = mContext;
    }

    @Override
    public VacinaRVA.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cartao_detalhe_item, null);
        VacinaRVA.SingleItemRowHolder mh = new VacinaRVA.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(VacinaRVA.SingleItemRowHolder holder, int i) {
        /*
        Vacina singleItem = vacinaList.get(i);
        holder.textVacina.setText(singleItem.getVaciNome());
        holder.textRede.setText(singleItem.getVaciRede());
        String rede = null;
        String vacinar = null;
        */

        //Log.d("VACINA NOME: ", singleItem.getVaciNome());


       // List<Vacina> listaVacina = new ArrayList<Vacina>();
        //List<Dose> listaDose = new ArrayList<Dose>();
        //List<Idade> listaIdade = new ArrayList<Idade>();


        //List<Calendario>  allSampleData = new ArrayList<Calendario>();

        /*
        for (Idade idade : idadeList) {
           // Calendario calendario = new Calendario();
            //calendario.setHeaderTitulo(idade.getIdadDescricao());
            RealmList<Vacina> vacinaItem = new RealmList<Vacina>();
            for (Calendario calendarioItem : calendarioList) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    vacinaItem.add(calendarioItem.getVacina());

                    Log.d("VACINA ", calendarioItem.getVacina().getVaciNome());
                    Log.d("DOSE   ", calendarioItem.getDose().getDoseDescricao());
                    Log.d("IDADE  ", calendarioItem.getIdade().getIdadDescricao());
                    Log.d("REDE   ", calendarioItem.getVacina().getVaciRede());
                    Log.d("", "------------------------------------");

                    listaVacina.add(calendarioItem.getVacina());
                    listaDose.add(calendarioItem.getDose());
                    listaIdade.add(calendarioItem.getIdade());

                }
            }
            ///calendario.setVacinasInSection(vacinaItem);
            //allSampleData.add(calendario);
        }

        this.vacinaList = listaVacina;
        */

        String rede = new String();
        String vacinar = new String();

        if("ao nascer".equals(idadeList.get(i).getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar";
        else
            vacinar = "Vacinar com";
        if("9 a 14 anos".equals(idadeList.get(i).getIdadDescricao().toLowerCase()) || "11 a 14 anos".equals(idadeList.get(i).getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar entre";
        if("Pública".equals(vacinaList.get(i).getVaciRede()))
            rede = "Disponível na rede";
        else
            rede = "Opcional na rede";

        holder.textVacina.setText(vacinaList.get(i).getVaciNome());
        holder.textDose.setText(doseList.get(i).getDoseDescricao());
        holder.textIdade.setText(vacinar+" "+ idadeList.get(i).getIdadDescricao());
        holder.textRede.setText(rede + " "+vacinaList.get(i).getVaciRede());
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView imageItem;
        protected TextView textVacina;
        protected TextView textDose;
        protected TextView textIdade;
        protected TextView textRede;


        public SingleItemRowHolder(View view) {
            super(view);
            //this.imageItem = view.findViewById(R.id.image_item);
            this.textVacina = view.findViewById(R.id.text_vacina);
            this.textDose = view.findViewById(R.id.text_dose);
            this.textIdade = view.findViewById(R.id.text_idade);
            this.textRede = view.findViewById(R.id.text_rede);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(ImunizacaoActivity.getStartIntent(mContext));
                    //((Activity)mContext).finish();
                }
            });

        }

    }
}

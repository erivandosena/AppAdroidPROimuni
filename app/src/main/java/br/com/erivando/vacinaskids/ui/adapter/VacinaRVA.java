package br.com.erivando.vacinaskids.ui.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Imunizacao;
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
    private List<Imunizacao> imunizacaoList;
    private Cartao cartaoVacinal;
    private int limiteRemove;

    private Context mContext;
    private ProgressDialog progressDialog;

    public VacinaRVA(List<Vacina> vacinaList, List<Dose> doseList, List<Idade> idadeList, List<Imunizacao> imunizacaoList, Cartao cartaoVacinal, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.imunizacaoList = imunizacaoList;
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
    public void onBindViewHolder(VacinaRVA.SingleItemRowHolder holder, final int i) {
        String rede = new String();
        String vacinar = new String();

        if ("ao nascer".equals(idadeList.get(i).getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar";
        else
            vacinar = "Vacinar com";
        if ("9 a 14 anos".equals(idadeList.get(i).getIdadDescricao().toLowerCase()) || "11 a 14 anos".equals(idadeList.get(i).getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar entre";
        if ("Pública".equals(vacinaList.get(i).getVaciRede()))
            rede = "Disponível na rede";
        else
            rede = "Opcional na rede";

        // VERIFICAÇÃO VENCIMENTO VACINAS
        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartaoVacinal.getCrianca().getCriaNascimento().getTime()) / (1000L*60*60*24*365/12);
        String mesesIdadeCalendario = idadeList.get(i).getIdadDescricao().toLowerCase().trim();

        holder.imageVacina.setImageResource(R.drawable.ic_vacina);

        if(!imunizacaoList.isEmpty()) {

            if("ao nascer".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {

                        if(mesesIdadeCrianca <= 1L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);

                        if(mesesIdadeCrianca > 1L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("2 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 2L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 2L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("3 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 3L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 3L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("4 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 4L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 4L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("5 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 5L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 5L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("6 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 6L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 6L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("7 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 7L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 7L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("9 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 9L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 9L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("12 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 12L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 12L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("15 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 15L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 15L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("18 meses".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 18L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 18L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("4 anos".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 48L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 48L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("5 anos".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 60L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 60L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("11 anos".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 132L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 132L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("9 a 14 anos".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 108L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if((mesesIdadeCrianca > 108L && mesesIdadeCrianca <= 168L))
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if("11 a 14 anos".equals(mesesIdadeCalendario)) {
                for (Imunizacao imunizacao : imunizacaoList) {
                    if (imunizacao.getVacina().getId() == vacinaList.get(i).getId() && imunizacao.getDose().getId() == doseList.get(i).getId())
                        holder.imageVacina.setImageResource(R.drawable.ic_vacina_imunizada);
                    else {
                        if(mesesIdadeCrianca == 132L )
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_aviso);
                        if(mesesIdadeCrianca > 132L && mesesIdadeCrianca <= 168L)
                            holder.imageVacina.setImageResource(R.drawable.ic_vacina_vencida);
                    }
                }
            }

            if ("menino".equals(cartaoVacinal.getCrianca().getCriaSexo().toLowerCase())) {
                if ("9 a 14 anos".equals(mesesIdadeCalendario)) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            deleteItem(i);
                        }
                    });
                }
            }

            if ("menina".equals(cartaoVacinal.getCrianca().getCriaSexo().toLowerCase())) {
                if ("11 a 14 anos".equals(mesesIdadeCalendario)) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            deleteItem(i);
                        }
                    });
                }
            }
        } else {

        }
        // VERIFICAÇÃO VENCIMENTO VACINAS

        holder.textVacina.setText(vacinaList.get(i).getVaciNome());
        holder.textDose.setText(doseList.get(i).getDoseDescricao());
        holder.textIdade.setText(vacinar + " " + idadeList.get(i).getIdadDescricao());
        holder.textRede.setText(rede + " " + vacinaList.get(i).getVaciRede());

        holder.idVacina = vacinaList.get(i).getId();
        holder.idDose = doseList.get(i).getId();
        holder.idIdade = idadeList.get(i).getId();
        holder.idCartao = cartaoVacinal.getId();
    }

    public void deleteItem(int position) {
        limiteRemove +=1;
        if(limiteRemove == 1) {
            Log.e("position", String.valueOf(position));
            vacinaList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected CardView cardView;
        protected ImageView imageVacina;
        protected TextView textVacina;
        protected TextView textDose;
        protected TextView textIdade;
        protected TextView textRede;

        protected Long idVacina;
        protected Long idDose;
        protected Long idIdade;
        protected Long idCartao;

        public SingleItemRowHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.imageVacina = view.findViewById(R.id.imagem_vacina);
            this.textVacina = view.findViewById(R.id.text_vacina);
            this.textDose = view.findViewById(R.id.text_dose);
            this.textIdade = view.findViewById(R.id.text_idade);
            this.textRede = view.findViewById(R.id.text_rede);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ImunizacaoActivity.getStartIntent(mContext);
                    intent.putExtra("vacina", idVacina);
                    intent.putExtra("dose", idDose);
                    intent.putExtra("idade", idIdade);
                    intent.putExtra("cartao", idCartao);
                    mContext.startActivity(intent);
                }
            });

        }

    }
}

package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoActivity;

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

    private Vacina vacina;
    private Dose dose;
    private Idade idade;
    private Cartao cartao;

    private Context mContext;

    protected CardView cardView;

    private boolean redeVacinas;

    private List<Integer> itensRemove;

    private int countItemHolder;

    private boolean startRemove;

    private List<Imunizacao> imunizacoesHPV;


    public VacinaRVA(List<Vacina> vacinaList, List<Dose> doseList, List<Idade> idadeList, List<Imunizacao> imunizacaoList, Cartao cartao, boolean redeVacinas, List<Imunizacao> imunizacoesHPV, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.imunizacaoList = imunizacaoList;
        this.cartao = cartao;
        this.mContext = mContext;
        this.redeVacinas = redeVacinas;
        this.imunizacoesHPV = imunizacoesHPV;
        this.itensRemove = new ArrayList<Integer>();
    }

    @Override
    public VacinaRVA.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cartao_detalhe_item, null);
        VacinaRVA.SingleItemRowHolder mh = new VacinaRVA.SingleItemRowHolder(view);
        this.cardView = view.findViewById(R.id.card_view_cartao_vacina);
        return mh;
    }

    @Override
    public void onBindViewHolder(VacinaRVA.SingleItemRowHolder holder, final int i) {
        countItemHolder += 1;

        vacina = vacinaList.get(i);
        dose = doseList.get(i);
        idade = idadeList.get(i);

        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
        String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase().trim();

        verificaCartaoVacinal(mesesIdadeCrianca, mesesIdadeCalendario, holder);

        if ("Privada".equalsIgnoreCase(vacina.getVaciRede())) {
            if (!redeVacinas) {
                if (!itensRemove.contains(i))
                    itensRemove.add(i);
            }
        }
        if ("Pneumocócica 23V".equalsIgnoreCase(vacina.getVaciNome())) {
            if (!cartao.getCrianca().isCriaEtnia()) {
                if (!itensRemove.contains(i))
                    itensRemove.add(i);
            }
        }
        if ("HPV".equalsIgnoreCase(vacina.getVaciNome())) {
            if ("Menino".equalsIgnoreCase(cartao.getCrianca().getCriaSexo())) {
                if ("9 a 14 anos".equals(mesesIdadeCalendario)) {
                    if (!itensRemove.contains(i))
                        itensRemove.add(i);
                }
            }
            if ("Menina".equalsIgnoreCase(cartao.getCrianca().getCriaSexo())) {
                if ("11 a 14 anos".equals(mesesIdadeCalendario)) {
                    if (!itensRemove.contains(i))
                        itensRemove.add(i);
                }
            }


        }

        Collections.sort(itensRemove);
        Collections.reverse(itensRemove);

        startRemove = (getItemCount() == countItemHolder);
        if (startRemove) {
            for (Integer posicao : itensRemove)
                removeItem(posicao);
        }

        String vacinar;
        if ("ao nascer".equalsIgnoreCase(idade.getIdadDescricao()))
            vacinar = "Vacinar";
        else
            vacinar = "Vacinar com";
        if ("2 anos".equalsIgnoreCase(idade.getIdadDescricao()))
            vacinar = "Vacinar a partir de";
        if ("9 a 14 anos".equalsIgnoreCase(idade.getIdadDescricao()) || "11 a 14 anos".equalsIgnoreCase(idade.getIdadDescricao()))
            vacinar = "Vacinar entre";

        /*
        String rede;
        if ("Pública".equalsIgnoreCase(vacina.getVaciRede()))
            rede = "Disponível na rede pública e privada";
        else
            rede = "Opcional na rede privada";
            */

        holder.textVacina.setText(vacina.getVaciNome());
        holder.textDose.setText(dose.getDoseDescricao());
        holder.textIdade.setText(vacinar + " " + idade.getIdadDescricao());
        //holder.textRede.setText(rede);

        holder.idVacina = vacina.getId();
        holder.idDose = dose.getId();
        holder.idIdade = idade.getId();
        holder.idCartao = cartao.getId();
    }

    private void removeItem(final int position) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    vacinaList.remove(position);
                    doseList.remove(position);
                    idadeList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, getItemCount());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void verificaCartaoVacinal(Long mesesIdadeCrianca, String mesesIdadeCalendario, SingleItemRowHolder holder) {
        switch (mesesIdadeCalendario) {
            case "ao nascer":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 1L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 1L, null, holder);
                }
                break;
            case "2 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 2L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 2L, null, holder);
                }
                break;
            case "3 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 3L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 3L, null, holder);
                }
                break;
            case "4 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 4L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 4L, null, holder);
                }
                break;
            case "5 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 5L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 5L, null, holder);
                }
                break;
            case "6 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 6L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 6L, null, holder);
                }
                break;
            case "7 meses":
                System.out.println("7 meses");
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 7L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 7L, null, holder);
                }
                break;
            case "9 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 9L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 9L, null, holder);
                }
                break;
            case "12 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 12L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 12L, null, holder);
                }
                break;
            case "15 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 15L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 15L, null, holder);
                }
                break;
            case "18 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 18L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 18L, null, holder);
                }
                break;
            case "2 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 24L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 24L, null, holder);
                }
                break;
            case "4 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 48L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 48L, null, holder);
                }
                break;
            case "5 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 60L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 60L, null, holder);
                }
                break;
            case "11 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, null, holder);
                }
                break;
            case "9 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, imunizacao, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 108L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 108L, null, holder);
                }
                break;
            case "11 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, imunizacao, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, null, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, null, holder);
                }
                break;
            default:
                break;
        }
    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas, Imunizacao imunizacao, SingleItemRowHolder holder) {

        int vacinaCinzaAvencer = mContext.getResources().getColor((R.color.colorGray));
        int vacinaVerdeEmdia = mContext.getResources().getColor((R.color.colorGreen));
        int vacinaAmareloVencendo = mContext.getResources().getColor((R.color.colorYellow));
        int vacinaVermelhoVencido = mContext.getResources().getColor((R.color.colorRedGoogleLight));

        cardView.setCardBackgroundColor(vacinaCinzaAvencer);
        holder.textStatus.setText("Aguardando prazo");

        if (mesesIdadeCrianca == null && semanas == null) {
            if (!imunizacaoList.isEmpty()) {
                cardView.setCardBackgroundColor(vacinaVerdeEmdia);
                holder.textStatus.setText("Aplicada");

                if(imunizacao != null) {
                    if ("9 a 14 anos".equalsIgnoreCase(imunizacao.getIdade().getIdadDescricao())) {
                        if (!imunizacoesHPV.isEmpty() && isImunizacoes() < 2) {
                            cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                            holder.textStatus.setText("Atrasada");
                        }
                    }
                    if ("11 a 14 anos".equalsIgnoreCase(imunizacao.getIdade().getIdadDescricao())) {
                        if (!imunizacoesHPV.isEmpty() && isImunizacoes() < 2) {
                            cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                            holder.textStatus.setText("Atrasada");
                        }
                    }
                }
            }
        } else {

            if(semanas <= 1L) {
                if (mesesIdadeCrianca <= 1L) {
                    cardView.setCardBackgroundColor(vacinaAmareloVencendo);
                    holder.textStatus.setText("Aguardando aplicação");
                } else if (mesesIdadeCrianca > 1L) {
                    cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                    holder.textStatus.setText("Atrasada");
                }
            }
            if (mesesIdadeCrianca == semanas) {
                cardView.setCardBackgroundColor(vacinaAmareloVencendo);
                holder.textStatus.setText("Aguardando aplicação");
            }
            if (mesesIdadeCrianca > semanas) {
                cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                holder.textStatus.setText("Atrasada");
            }
        }

        cardView.setRadius(20f);
        cardView.setCardElevation(2f);
        cardView.setUseCompatPadding(true);
    }

    private int isImunizacoes() {
        return imunizacoesHPV.size();
    }

    private boolean verificaCartao(Imunizacao imunizacao) {
        return (imunizacao.getCartao().getCrianca().getId().equals(cartao.getCrianca().getId()));
    }

    private boolean verificaImunizacao(Imunizacao imunizacao) {
        return (imunizacao.getVacina().getId().equals(vacina.getId()) && imunizacao.getDose().getId().equals(dose.getId()));
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView textVacina;
        protected TextView textDose;
        protected TextView textIdade;
        //protected TextView textRede;
        protected TextView textStatus;

        protected Long idVacina;
        protected Long idDose;
        protected Long idIdade;
        protected Long idCartao;

        public SingleItemRowHolder(View view) {
            super(view);
            this.textVacina = view.findViewById(R.id.text_vacina);
            this.textDose = view.findViewById(R.id.text_dose);
            this.textIdade = view.findViewById(R.id.text_idade);
            //this.textRede = view.findViewById(R.id.text_rede);
            this.textStatus = view.findViewById(R.id.text_status);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ImunizacaoActivity.getStartIntent(mContext);
                    intent.putExtra("vacina", idVacina);
                    intent.putExtra("dose", idDose);
                    intent.putExtra("idade", idIdade);
                    intent.putExtra("cartao", idCartao);
                    mContext.startActivity(intent);
                    ((CartaoDetalheActivity) mContext).finish();

                }
            });

        }

    }
}

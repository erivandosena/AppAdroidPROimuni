package br.com.erivando.proimuni.ui.adapter;

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

        verificaCartaoVacinal(mesesIdadeCrianca, mesesIdadeCalendario);

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
                    //removeItem(i);
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
        if ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar";
        else
            vacinar = "Vacinar com";
        if ("9 a 14 anos".equals(idade.getIdadDescricao().toLowerCase()) || "11 a 14 anos".equals(idade.getIdadDescricao().toLowerCase()))
            vacinar = "Vacinar entre";

        String rede;
        if ("Pública".equals(vacina.getVaciRede()))
            rede = "Disponível na rede";
        else
            rede = "Opcional na rede";

        holder.textVacina.setText(vacina.getVaciNome());
        holder.textDose.setText(dose.getDoseDescricao());
        holder.textIdade.setText(vacinar + " " + idade.getIdadDescricao());
        holder.textRede.setText(rede + " " + vacina.getVaciRede());

        holder.idVacina = vacina.getId();
        holder.idDose = dose.getId();
        holder.idIdade = idade.getId();
        holder.idCartao = cartao.getId();
    }

    private void removeItem(final int position) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                vacinaList.remove(position);
                doseList.remove(position);
                idadeList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, getItemCount());
                //Log.e("REMOVIDO ", String.valueOf(position));
            }
        });
    }

    private void verificaCartaoVacinal(Long mesesIdadeCrianca, String mesesIdadeCalendario) {
        switch (mesesIdadeCalendario) {
            case "ao nascer":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 1L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 1L, null);
                }
                break;
            case "2 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 2L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 2L, null);
                }
                break;
            case "3 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 3L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 3L, null);
                }
                break;
            case "4 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 4L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 4L, null);
                }
                break;
            case "5 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 5L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 5L, null);
                }
                break;
            case "6 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 6L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 6L, null);
                }
                break;
            case "7 meses":
                System.out.println("7 meses");
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 7L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 7L, null);
                }
                break;
            case "9 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 9L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 9L, null);
                }
                break;
            case "12 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 12L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 12L, null);
                }
                break;
            case "15 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 15L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 15L, null);
                }
                break;
            case "18 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 18L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 18L, null);
                }
                break;
            case "4 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 48L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 48L, null);
                }
                break;
            case "5 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 60L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 60L, null);
                }
                break;
            case "11 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, null);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, null);
                }
                break;
            case "9 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, imunizacao);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 108L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 108L, null);
                }
                break;
            case "11 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, imunizacao);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, null);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, null);
                }
                break;
            default:
                break;
        }
    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas, Imunizacao imunizacao) {

        int vacinaCinzaAvencer = mContext.getResources().getColor((R.color.colorGray));
        int vacinaVerdeEmdia = mContext.getResources().getColor((R.color.colorGreen));
        int vacinaAmareloVencendo = mContext.getResources().getColor((R.color.colorYellow));
        int vacinaVermelhoVencido = mContext.getResources().getColor((R.color.colorPink));

        cardView.setCardBackgroundColor(vacinaCinzaAvencer);
        if (mesesIdadeCrianca == null && semanas == null) {
            if (!imunizacaoList.isEmpty()) {
                cardView.setCardBackgroundColor(vacinaVerdeEmdia);

                if(imunizacao != null) {
                    if ("9 a 14 anos".equalsIgnoreCase(imunizacao.getIdade().getIdadDescricao())) {
                        if (!imunizacoesHPV.isEmpty() && isImunizacoes() < 2) {
                            cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                        }
                    }
                    if ("11 a 14 anos".equalsIgnoreCase(imunizacao.getIdade().getIdadDescricao())) {
                        if (!imunizacoesHPV.isEmpty() && isImunizacoes() < 2) {
                            cardView.setCardBackgroundColor(vacinaVermelhoVencido);
                        }
                    }
                }
            }
        } else {
            if (mesesIdadeCrianca == semanas) {
                cardView.setCardBackgroundColor(vacinaAmareloVencendo);
            }
            if (mesesIdadeCrianca > semanas) {
                cardView.setCardBackgroundColor(vacinaVermelhoVencido);
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
        protected TextView textRede;

        protected Long idVacina;
        protected Long idDose;
        protected Long idIdade;
        protected Long idCartao;

        public SingleItemRowHolder(View view) {
            super(view);
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
                    ((CartaoDetalheActivity) mContext).finish();

                }
            });

        }

    }
}

package br.com.erivando.proimuni.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
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
    private ProgressDialog progressDialog;

    private int limiteRemove;

    public VacinaRVA(List<Vacina> vacinaList, List<Dose> doseList, List<Idade> idadeList, List<Imunizacao> imunizacaoList, Cartao cartao, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.idadeList = idadeList;
        this.imunizacaoList = imunizacaoList;
        this.cartao = cartao;
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
        vacina = vacinaList.get(i);
        dose = doseList.get(i);
        idade = idadeList.get(i);

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

        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
        String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase().trim();

        verificaCartaoVacinal(mesesIdadeCrianca, mesesIdadeCalendario, holder);

        if ("menino".equals(cartao.getCrianca().getCriaSexo().toLowerCase())) {
            if ("9 a 14 anos".equals(mesesIdadeCalendario)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        deleteItem(i);
                    }
                });
            }
        }
        if ("menina".equals(cartao.getCrianca().getCriaSexo().toLowerCase())) {
            if ("11 a 14 anos".equals(mesesIdadeCalendario)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        deleteItem(i);
                    }
                });
            }
        }

        holder.textVacina.setText(vacina.getVaciNome());
        holder.textDose.setText(dose.getDoseDescricao());
        holder.textIdade.setText(vacinar + " " + idade.getIdadDescricao());
        holder.textRede.setText(rede + " " + vacina.getVaciRede());

        holder.idVacina = vacina.getId();
        holder.idDose = dose.getId();
        holder.idIdade = idade.getId();
        holder.idCartao = cartao.getId();
    }

    private void verificaCartaoVacinal(Long mesesIdadeCrianca, String mesesIdadeCalendario, VacinaRVA.SingleItemRowHolder holder) {
        switch (mesesIdadeCalendario) {
            case "ao nascer":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 1L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 1L, holder);
                }
                break;
            case "2 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 2L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 2L, holder);
                }
                break;
            case "3 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 3L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 3L, holder);
                }
                break;
            case "4 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 4L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 4L, holder);
                }
                break;
            case "5 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 5L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 5L, holder);
                }
                break;
            case "6 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 6L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 6L, holder);
                }
                break;
            case "7 meses":
                System.out.println("7 meses");
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 7L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 7L, holder);
                }
                break;
            case "9 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 9L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 9L, holder);
                }
                break;
            case "12 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 12L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 12L, holder);
                }
                break;
            case "15 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 15L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 15L, holder);
                }
                break;
            case "18 meses":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 18L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 18L, holder);
                }
                break;
            case "4 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 48L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 48L, holder);
                }
                break;
            case "5 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 60L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 60L, holder);
                }
                break;
            case "11 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, holder);
                }
                break;
            case "9 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 108L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 108L, holder);
                }
                break;
            case "11 a 14 anos":
                if (!imunizacaoList.isEmpty()) {
                    for (Imunizacao imunizacao : imunizacaoList) {
                        if (verificaCartao(imunizacao)) {
                            if (verificaImunizacao(imunizacao)) {
                                aplicaStatusVacina(null, null, holder);
                                break;
                            } else {
                                aplicaStatusVacina(mesesIdadeCrianca, 132L, holder);
                            }
                        }
                    }
                } else {
                    aplicaStatusVacina(mesesIdadeCrianca, 132L, holder);
                }
                break;
            default:
                break;
        }
    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas, VacinaRVA.SingleItemRowHolder holder) {
        int vacinaAvencer = R.drawable.ic_vacina;
        int vacinaEmDias = R.drawable.ic_vacina_imunizada;
        int vacinaVencendo = R.drawable.ic_vacina_aviso;
        int vacinaVencida = R.drawable.ic_vacina_vencida;

        holder.imageVacina.setImageResource(vacinaAvencer);
        if(mesesIdadeCrianca == null && semanas == null) {
            if (!imunizacaoList.isEmpty()) {
                holder.imageVacina.setImageResource(vacinaEmDias);
            }
        } else {
            if (mesesIdadeCrianca == semanas) {
                holder.imageVacina.setImageResource(vacinaVencendo);
            }
            if (mesesIdadeCrianca > semanas) {
                holder.imageVacina.setImageResource(vacinaVencida);
            }
            if ("9 a 14 anos".equals(mesesIdadeCrianca)) {
                if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 60L) {
                    holder.imageVacina.setImageResource(vacinaVencida);
                }
            }
            if ("11 a 14 anos".equals(mesesIdadeCrianca)) {
                if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 36L) {
                    holder.imageVacina.setImageResource(vacinaVencida);
                }
            }
        }
    }

    private boolean verificaCartao(Imunizacao imunizacao) {
        return (imunizacao.getCartao().getCrianca().getId().equals(cartao.getCrianca().getId()));
    }

    private boolean verificaImunizacao(Imunizacao imunizacao) {
        return (imunizacao.getVacina().getId().equals(vacina.getId()) && imunizacao.getDose().getId().equals(dose.getId()));
    }

    public void deleteItem(int position) {
        limiteRemove +=1;
        if(limiteRemove == 1) {
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

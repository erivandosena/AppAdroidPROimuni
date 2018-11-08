package br.com.erivando.vacinaskids.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Imunizacao;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.di.component.DaggerServiceComponent;
import br.com.erivando.vacinaskids.di.component.ServiceComponent;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.adapter.VacinaRVA;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   04 de Novembro de 2018 as 20:45
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class AlarmReceiver extends BroadcastReceiver {

    final String CANAL_ID = "NotificacoesID";

    @Inject
    IDataManager iDataManager;

    private Notification repeatedNotification;

    private Usuario usuario;
    private List<Crianca> criancas;
    private List<Vacina> vacinas;
    private List<Dose> doses;
    private List<Idade> idades;
    private List<Imunizacao> imunizacoes;
    private List<Calendario> calendarios;
    private List<Cartao> cartoes;
    private Imunizacao imunizacao;
    private Vacina vacina;
    private Dose dose;
    private Idade idade;
    private Crianca crianca;
    private Cartao cartao;

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceComponent component = DaggerServiceComponent.builder().applicationComponent(((AppAplicacao) AppAplicacao.contextApp).getComponent()).build();
        component.inject(this);

        usuario = iDataManager.obtemUsuario();
        String nomeUsuario = (usuario != null)? usuario.getUsuaNome() : "Olá";

        criancas = iDataManager.obtemCriancas();
        cartoes = iDataManager.obtemCartoes();
        idades = iDataManager.obtemIdades();
        calendarios = iDataManager.obtemCalendarios();
        vacinas = new ArrayList<Vacina>();
        doses = new ArrayList<Dose>();
        idades = new ArrayList<Idade>();
        imunizacoes = new ArrayList<Imunizacao>();

        for (Idade idade : idades) {
            for (Calendario calendarioItem : calendarios) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    vacinas.add(calendarioItem.getVacina());
                    doses.add(calendarioItem.getDose());
                    idades.add(calendarioItem.getIdade());
                    for (Cartao cartao:cartoes) {
                        imunizacao = iDataManager.obtemImunizacao(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), (cartao != null) ? cartao.getId() : 0L});
                        if (imunizacao != null) {
                            imunizacoes.add(imunizacao);
                        }
                    }
                }
            }
        }

        for (Cartao cartao:cartoes) {
            Log.e("CARTAO  ", cartao.getId().toString());
            for (Crianca crianca:criancas) {
                Log.e("CRIANCA  ", crianca.getCriaNome());
                if(cartao.getCrianca().getId().equals(crianca.getId())) {
                    //for (Imunizacao imunizacao:imunizacoes) {
                       // if(!cartao.getCrianca().getCriaNome().equals(imunizacao.getCartao().getCrianca().getCriaNome())){

                            Log.e("CRIANCA ", cartao.getCrianca().getCriaNome());
                            //Log.e("VACINA ", imunizacao.getVacina().getVaciDescricao());
                            //Log.e("IDADE ", imunizacao.getIdade().getIdadDescricao());

                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                            aplicaStatusVacina(mesesIdadeCrianca, 1L);

                       // }
                    //}
                }
            }
        }
        /*
        for (Vacina vacinaItem:vacinas) {
            for(Idade idade:idades) {
                String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase().trim();

                switch (mesesIdadeCalendario) {
                    case "ao nascer":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 1L);
                            }
                        }
                        break;
                    case "2 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 2L);
                            }
                        }
                        break;
                    case "3 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 3L);
                            }
                        }
                        break;
                    case "4 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 4L);
                            }
                        }
                        break;
                    case "5 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 5L);
                            }
                        }
                        break;
                    case "6 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 6L);
                            }
                        }
                        break;
                    case "7 meses":
                        System.out.println("7 meses");
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 7L);
                            }
                        }
                        break;
                    case "9 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 9L);
                            }
                        }
                        break;
                    case "12 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 12L);
                            }
                        }
                        break;
                    case "15 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 15L);
                            }
                        }
                        break;
                    case "18 meses":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 18L);
                            }
                        }
                        break;
                    case "4 anos":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 48L);
                            }
                        }
                        break;
                    case "5 anos":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 60L);
                            }
                        }
                        break;
                    case "11 anos":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 132L);
                            }
                        }
                        break;
                    case "9 a 14 anos":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {

                                        Log.e("Crianca ", imunizacao.getCartao().getCrianca().getCriaNome());
                                        Log.e("Vacina ", imunizacao.getVacina().getVaciNome());
                                        Log.e("Dose ", imunizacao.getDose().getDoseDescricao());
                                        Log.e("Idade ", imunizacao.getIdade().getIdadDescricao());

                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 108L);
                            }
                        }
                        break;
                    case "11 a 14 anos":
                        if (!imunizacoes.isEmpty()) {
                            for (Imunizacao imunizacao : imunizacoes) {
                                if (verificaCartao(imunizacao)) {
                                    if (verificaImunizacao(imunizacao)) {
                                        aplicaStatusVacina(null, null);
                                        break;
                                    } else {
                                        for (Cartao cartao:cartoes) {
                                            Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (Cartao cartao:cartoes) {
                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                aplicaStatusVacina(mesesIdadeCrianca, 132L);
                            }
                        }
                        break;
                    default:
                        break;
                }



            }


        }
        */


        // notificação inicial geral
        repeatedNotification = showLocalNotification(context, CartaoDetalheActivity.class,
                nomeUsuario+", inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose realizada no posto de saúde ou em clínica particular.",
                "Cartão Vacinal",
                "Lembrete!",
                "Cadastre as imunizações realizadas!"
        ).build();

        // Envia notificações
        if (imunizacoes.isEmpty())
            NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_ELAPSED, repeatedNotification);


    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas) {
      //  int vacinaAvencer = R.drawable.ic_vacina;
     //   int vacinaEmDias = R.drawable.ic_vacina_imunizada;
      //  int vacinaVencendo = R.drawable.ic_vacina_aviso;
      //  int vacinaVencida = R.drawable.ic_vacina_vencida;

       // holder.imageVacina.setImageResource(vacinaAvencer);
        if(mesesIdadeCrianca == null && semanas == null) {
            if (!imunizacoes.isEmpty()) {
                //holder.imageVacina.setImageResource(vacinaEmDias);
                Log.e("Vacina","OK!");
            }
        } else {
            if (mesesIdadeCrianca == semanas) {
                //holder.imageVacina.setImageResource(vacinaVencendo);
                Log.e("Vacina","VENCENDO!");
            }
            if (mesesIdadeCrianca > semanas) {
               // holder.imageVacina.setImageResource(vacinaVencida);
                Log.e("Vacina","VENCIDA!");
            }
            if ("9 a 14 anos".equals(mesesIdadeCrianca)) {
                if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 60L) {
                    //holder.imageVacina.setImageResource(vacinaVencida);
                    Log.e("Vacina","VENCIDA!");
                }
            }
            if ("11 a 14 anos".equals(mesesIdadeCrianca)) {
                if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 36L) {
                    //holder.imageVacina.setImageResource(vacinaVencida);
                    Log.e("Vacina","VENCIDA!");
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, CANAL_ID);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.bigText(textBig);
        bigText.setBigContentTitle(textContentTitle);
        bigText.setSummaryText(summaryTextTitle);

        mBuilder.setContentText(contentText);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[]{100, 300, 500, 800, 1000});
        mBuilder.setLights(0xff0080FF, 2000, 3000);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        // Cria uma intenção explícita para uma atividade do aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, clsDestino);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, NotificationHelper.ALARM_TYPE_ELAPSED, stackBuilder.getIntents(), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        return mBuilder;
    }

}

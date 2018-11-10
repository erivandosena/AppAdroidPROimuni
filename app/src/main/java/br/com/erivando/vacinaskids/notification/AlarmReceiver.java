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
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;

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

    private List<Crianca> criancas;
    private List<Vacina> vacinas;
    private List<Dose> doses;
    private List<Idade> idades, listaIdade;
    private List<Imunizacao> imunizacoes;
    private List<Calendario> calendarios;
    private List<Cartao> cartoes;

    private Usuario usuario;
    private Imunizacao imunizacao;
    private Idade idade;
    private Cartao cartao;

    private List<String> statusCartaoVacinal;

    @Override
    public void onReceive(final Context context, Intent intent) {
        ServiceComponent component = DaggerServiceComponent.builder().applicationComponent(((AppAplicacao) AppAplicacao.contextApp).getComponent()).build();
        component.inject(this);

        usuario = iDataManager.obtemUsuario();
        String nomeUsuario = (usuario != null) ? usuario.getUsuaNome() : "Olá";

        criancas = iDataManager.obtemCriancas();
        cartoes = iDataManager.obtemCartoes();
        listaIdade = iDataManager.obtemIdades();
        calendarios = iDataManager.obtemCalendarios();

        imunizacoes = new ArrayList<Imunizacao>();
        vacinas = new ArrayList<Vacina>();
        doses = new ArrayList<Dose>();
        idades = new ArrayList<Idade>();

        for (Idade idade : listaIdade) {
            for (Calendario calendarioItem : calendarios) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    vacinas.add(calendarioItem.getVacina());
                    doses.add(calendarioItem.getDose());
                    idades.add(calendarioItem.getIdade());
                    for (Cartao cartao : cartoes) {
                        imunizacao = iDataManager.obtemImunizacao(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), (cartao != null) ? cartao.getId() : 0L});
                        if (imunizacao != null) {
                            imunizacoes.add(imunizacao);
                        }
                    }
                }
            }
        }

        // notificação inicial geral
        repeatedNotification = showLocalNotification(context, CartaoDetalheActivity.class,
                nomeUsuario + ", inicie os registros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose realizada no posto de saúde ou em clínica particular.",
                "Cartão Vacinal",
                "Lembrete!",
                "Cadastre as imunizações realizadas!"
        ).build();

        // Envia notificação
        if (imunizacoes.isEmpty()) {
            NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_ELAPSED, repeatedNotification);
        } else {
            statusCartaoVacinal = new ArrayList<String>();
            for (Idade idade : listaIdade) {
                for (Calendario calendarioItem : calendarios) {
                    if (calendarioItem.getIdade().getId() == idade.getId()) {
                        String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase().trim();
                        for (Cartao cartao : cartoes) {
                            for (Crianca crianca : criancas) {
                                if (cartao.getCrianca().getId().equals(crianca.getId())) {
                                    this.cartao = cartao;
                                    this.idade = idade;
                                    switch (mesesIdadeCalendario) {
                                        case "ao nascer":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                            }
                                            break;
                                        case "2 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                            }
                                            break;
                                        case "3 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                            }
                                            break;
                                        case "4 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                            }
                                            break;
                                        case "5 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                            }
                                            break;
                                        case "6 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                            }
                                            break;
                                        case "7 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                            }
                                            break;
                                        case "9 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                            }
                                            break;
                                        case "12 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                            }
                                            break;
                                        case "15 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                            }
                                            break;
                                        case "18 meses":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                            }
                                            break;
                                        case "4 anos":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                            }
                                            break;
                                        case "5 anos":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                            }
                                            break;
                                        case "11 anos":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                            }
                                            break;
                                        case "9 a 14 anos":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                            }
                                            break;
                                        case "11 a 14 anos":
                                            if (!imunizacoes.isEmpty()) {
                                                for (Imunizacao imunizacao : imunizacoes) {
                                                    if (!verificaCartao(imunizacao)) {
                                                        Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                        aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
            final List<String> listaVacina = new ArrayList<String>();
            for (Cartao cartao : cartoes) {
                for (int i = 0; i < statusCartaoVacinal.size(); i++) {
                    if (statusCartaoVacinal.get(i).toLowerCase().contains(cartao.getCrianca().getCriaNome().toLowerCase())) {
                        for (Calendario calendario : calendarios) {
                            if (statusCartaoVacinal.get(i).toLowerCase().contains(calendario.getIdade().getIdadDescricao().toLowerCase())) {
                                // Log.e("CRIANÇA;", cartao.getCrianca().getCriaNome());
                                // Log.e("IDADE;", calendario.getIdade().getIdadDescricao());
                                // Log.e("VACINA;", calendario.getVacina().getVaciNome());
                                // Log.e("DOSE;", calendario.getDose().getDoseDescricao());
                                // Log.e("REDE;", calendario.getVacina().getVaciRede());
                                listaVacina.add(calendario.getVacina().getVaciNome());
                            }
                        }
                        //Log.e("CARTAO:", cartao.getId() + " Olá! " + usuario.getUsuaNome() + "," + statusCartaoVacinal.get(i) + "\nAtenção para: " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e"));
                        // notificação por etatus de vacina
                        repeatedNotification = showLocalNotification(context, CartaoDetalheActivity.class,
                                "Olá! " + usuario.getUsuaNome() + "," + statusCartaoVacinal.get(i) + "\nAtenção para: " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e"),
                                "Cartão Vacinal",
                                "Lembrete!",
                                cartao.getCrianca().getCriaNome() + " precisa ser vacina com " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e") + "."
                        ).build();

                        try {
                            Log.d("Thread", "INICIA MENSAGENS");
                            // Envia notificações
                            NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_ELAPSED, repeatedNotification);
                            listaVacina.clear();
                            Thread.sleep(15000); //15Seg
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            Log.d("Thread", "FINALIZA MENSAGENS");
                        }
                    }
                }
            }
        }
    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas) {
        String statusVencendo = idade.getIdadDescricao().toUpperCase() + " vencendo o prazo.";
        String statusVencido = idade.getIdadDescricao().toUpperCase() + " com prazo vencido!";

        if (mesesIdadeCrianca == semanas) {
            statusCartaoVacinal.add(" fique atento para vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencendo : " necessária na idade de " + statusVencendo));
        }
        if (mesesIdadeCrianca > semanas) {
            statusCartaoVacinal.add(" mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
        }
        if ("9 a 14 anos".equals(mesesIdadeCrianca)) {
            if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 60L) {
                statusCartaoVacinal.add(" mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
            }
        }
        if ("11 a 14 anos".equals(mesesIdadeCrianca)) {
            if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 36L) {
                statusCartaoVacinal.add(" mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
            }
        }
    }

    private boolean verificaCartao(Imunizacao imunizacao) {
        return (imunizacao.getCartao().getCrianca().getId().equals(cartao.getCrianca().getId()));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, CANAL_ID);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText(textBig);
        bigTextStyle.setBigContentTitle(textContentTitle);
        bigTextStyle.setSummaryText(summaryTextTitle);

        mBuilder.setContentText(contentText);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setStyle(bigTextStyle);
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

package br.com.erivando.proimuni.di.component;

import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.di.module.ActivityModule;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaImunizacao;
import br.com.erivando.proimuni.ui.activity.dose.DoseActivity;
import br.com.erivando.proimuni.ui.activity.idade.IdadeActivity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoActivity;
import br.com.erivando.proimuni.ui.activity.login.LoginActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.activity.mapa.MapaActivity;
import br.com.erivando.proimuni.ui.activity.notificacao.NotificacaoActivity;
import br.com.erivando.proimuni.ui.activity.splash.SplashActivity;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioActivity;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaActivity;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaDetalheActivity;
import br.com.erivando.proimuni.ui.fragment.sobre.Sobre;
import br.com.erivando.proimuni.ui.fragment.vacina.VacinaPrivadaFragment;
import br.com.erivando.proimuni.ui.fragment.vacina.VacinaPublicaFragment;
import dagger.Component;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:54
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activityInject);

    void inject(LoginActivity activityInject);

    void inject(SplashActivity activityInject);

    void inject(CadastroUsuarioActivity activityInject);

    void inject(Sobre fragmentInject);

    void inject(CriancaActivity activityInject);

    void inject(CriancaListaActvity activityInject);

    void inject(CartaoActivity activityInject);

    void inject(CalendarioActivity activityInject);

    void inject(VacinaActivity activityInject);

    void inject(IdadeActivity activityInject);

    void inject(DoseActivity activityInject);

    void inject(CartaoListaActvity activityInject);

    void inject(ImunizacaoActivity activityInject);

    void inject(VacinaDetalheActivity activityInject);

    void inject(VacinaPublicaFragment fragmentInject);

    void inject(VacinaPrivadaFragment fragmentInject);

    void inject(CartaoDetalheActivity activityInject);

    void inject(MapaActivity activityInject);

    void inject(NotificacaoActivity activityInject);

    void inject(CriancaListaImunizacao activityInject);

}

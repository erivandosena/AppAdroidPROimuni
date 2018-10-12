package br.com.erivando.vacinaskids.di.component;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.di.module.ActivityModule;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoActivity;
import br.com.erivando.vacinaskids.ui.acoes.calendario.CalendarioActivity;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoListaActvity;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaActivity;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.ui.acoes.dose.DoseActivity;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeActivity;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaActivity;
import br.com.erivando.vacinaskids.ui.login.LoginActivity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import br.com.erivando.vacinaskids.ui.sobre.SobreFragment;
import br.com.erivando.vacinaskids.ui.splash.SplashActivity;
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

    void inject(SobreFragment activityInject);

    void inject(CriancaActivity activityInject);

    void inject(CriancaListaActvity activityInject);

    void inject(CartaoActivity activityInject);

    void inject(CalendarioActivity activityInject);

    void inject(VacinaActivity activityInject);

    void inject(IdadeActivity activityInject);

    void inject(DoseActivity activityInject);

    void inject(CartaoListaActvity activityInject);
}

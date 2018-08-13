package br.com.erivando.vacinaskids.di.component;

import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.di.module.ActivityModule;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoActivity;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaActivity;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioActivity;
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

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(SplashActivity splashActivity);

    void inject(CadastroUsuarioActivity cadastroUsuarioActivity);

    void inject(SobreFragment sobreFragment);

    void inject(CriancaActivity criancaActivity);

    void inject(CriancaListaActvity criancaListaActivity);

    void inject(CartaoActivity cartaoActivity);
}

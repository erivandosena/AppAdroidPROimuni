package br.com.erivando.vacinaskids.di.component;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.di.module.ActivityModule;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
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

}

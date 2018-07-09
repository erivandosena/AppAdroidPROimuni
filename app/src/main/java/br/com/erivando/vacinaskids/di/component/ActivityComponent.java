package br.com.erivando.vacinaskids.di.component;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.di.module.ActivityModule;
import br.com.erivando.vacinaskids.main.MainActivity;
import dagger.Component;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
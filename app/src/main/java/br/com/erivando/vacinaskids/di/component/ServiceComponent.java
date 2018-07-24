package br.com.erivando.vacinaskids.di.component;

import br.com.erivando.vacinaskids.di.PerService;
import br.com.erivando.vacinaskids.di.module.ServiceModule;
import br.com.erivando.vacinaskids.service.SyncService;
import dagger.Component;


/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 22:25
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(SyncService service);

}
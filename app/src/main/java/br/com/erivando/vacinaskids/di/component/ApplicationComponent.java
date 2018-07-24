package br.com.erivando.vacinaskids.di.component;

import android.content.Context;

import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.module.ApplicationModule;
import br.com.erivando.vacinaskids.service.SyncService;
import br.com.erivando.vacinaskids.ui.AppAplicacao;
import dagger.Component;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:54
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AppAplicacao appAplicacao);

    void inject(SyncService service);

    @ApplicationContext
    Context getContext();

    IDataManager getIDataManager();
}

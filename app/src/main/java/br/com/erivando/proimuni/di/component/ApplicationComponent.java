package br.com.erivando.proimuni.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.di.ApplicationContext;
import br.com.erivando.proimuni.di.module.ApplicationModule;
import br.com.erivando.proimuni.service.Servico;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
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

    void inject(Servico service);

    @ApplicationContext
    Context getContext();

    Application application();

    IDataManager getIDataManager();

}

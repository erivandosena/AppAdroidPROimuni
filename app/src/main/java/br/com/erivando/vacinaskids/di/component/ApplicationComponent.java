package br.com.erivando.vacinaskids.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import br.com.erivando.vacinaskids.data.DataManager;
import br.com.erivando.vacinaskids.data.DbHelper;
import br.com.erivando.vacinaskids.data.SharedPrefsHelper;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.module.ApplicationModule;
import br.com.erivando.vacinaskids.main.DemoDaggerApp;
import dagger.Component;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(DemoDaggerApp demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();

    DbHelper getDbHelper();

}
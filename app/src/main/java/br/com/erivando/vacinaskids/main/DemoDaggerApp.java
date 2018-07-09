package br.com.erivando.vacinaskids.main;


import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.data.DataManager;
import br.com.erivando.vacinaskids.di.component.ApplicationComponent;
import br.com.erivando.vacinaskids.di.component.DaggerApplicationComponent;
import br.com.erivando.vacinaskids.di.module.ApplicationModule;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:36
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class DemoDaggerApp extends Application {
    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager dataManager;

    public static DemoDaggerApp get(Context context) {
        return (DemoDaggerApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

}

package br.com.erivando.vacinaskids.ui;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.BuildConfig;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.di.component.ApplicationComponent;
import br.com.erivando.vacinaskids.di.component.DaggerApplicationComponent;
import br.com.erivando.vacinaskids.di.module.ApplicationModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class AppAplicacao extends Application {

    @Inject
    IDataManager iDataManager;

    @Inject
    CalligraphyConfig calligraphyConfig;

    private ApplicationComponent applicationComponent;

    public static AppAplicacao get(Context context) {
        return (AppAplicacao) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        CalligraphyConfig.initDefault(calligraphyConfig);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }

    // Necessário substituir o componente por um teste específico
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}

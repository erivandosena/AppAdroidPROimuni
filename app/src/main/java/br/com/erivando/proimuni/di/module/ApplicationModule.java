package br.com.erivando.proimuni.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.DataManager;
import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.IPreferencesHelper;
import br.com.erivando.proimuni.database.PreferencesHelper;
import br.com.erivando.proimuni.di.ApplicationContext;
import br.com.erivando.proimuni.di.PreferenceInfo;
import br.com.erivando.proimuni.util.AppConstants;
import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:55
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application app) {
        application = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    IDataManager provideIDataManager(DataManager dataManager) {
        return dataManager;
    }

    @Provides
    @Singleton
    IPreferencesHelper provideIPreferencesHelper(PreferencesHelper preferencesHelper) {
        return preferencesHelper;
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }
}

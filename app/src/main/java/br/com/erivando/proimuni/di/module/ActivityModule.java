package br.com.erivando.proimuni.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import br.com.erivando.proimuni.di.ActivityContext;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioMvpPresenter;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioMvpView;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioPresenter;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoMvpView;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoPresenter;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpView;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoPresenter;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaMvpView;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaPresenter;
import br.com.erivando.proimuni.ui.activity.curiosidade.CuriosidadeMvpPresenter;
import br.com.erivando.proimuni.ui.activity.curiosidade.CuriosidadeMvpView;
import br.com.erivando.proimuni.ui.activity.curiosidade.CuriosidadePresenter;
import br.com.erivando.proimuni.ui.activity.dose.DoseMvpPresenter;
import br.com.erivando.proimuni.ui.activity.dose.DoseMvpView;
import br.com.erivando.proimuni.ui.activity.dose.DosePresenter;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpView;
import br.com.erivando.proimuni.ui.activity.idade.IdadePresenter;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoPresenter;
import br.com.erivando.proimuni.ui.activity.introducao.IntroducaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.introducao.IntroducaoMvpView;
import br.com.erivando.proimuni.ui.activity.introducao.IntroducaoPresenter;
import br.com.erivando.proimuni.ui.activity.login.LoginMvpPresenter;
import br.com.erivando.proimuni.ui.activity.login.LoginMvpView;
import br.com.erivando.proimuni.ui.activity.login.LoginPresenter;
import br.com.erivando.proimuni.ui.activity.main.MainMvpPresenter;
import br.com.erivando.proimuni.ui.activity.main.MainMvpView;
import br.com.erivando.proimuni.ui.activity.main.MainPresenter;
import br.com.erivando.proimuni.ui.activity.mapa.MapaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.mapa.MapaMvpView;
import br.com.erivando.proimuni.ui.activity.mapa.MapaPresenter;
import br.com.erivando.proimuni.ui.activity.sobre.SobreMvpPresenter;
import br.com.erivando.proimuni.ui.activity.sobre.SobreMvpView;
import br.com.erivando.proimuni.ui.activity.sobre.SobrePresenter;
import br.com.erivando.proimuni.ui.activity.splash.SplashMvpPresenter;
import br.com.erivando.proimuni.ui.activity.splash.SplashMvpView;
import br.com.erivando.proimuni.ui.activity.splash.SplashPresenter;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioMvpPresenter;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioMvpView;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioPresenter;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:55
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Module
public class ActivityModule {

    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(
            SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CadastroUsuarioMvpPresenter<CadastroUsuarioMvpView> provideCadastroUsuarioPresenter(CadastroUsuarioPresenter<CadastroUsuarioMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SobreMvpPresenter<SobreMvpView> provideSobrePresenter(SobrePresenter<SobreMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CriancaMvpPresenter<CriancaMvpView> provideCriancaPresenter(CriancaPresenter<CriancaMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CartaoMvpPresenter<CartaoMvpView> provideCartaoPresenter(CartaoPresenter<CartaoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CalendarioMvpPresenter<CalendarioMvpView> provideCalendarioPresenter(CalendarioPresenter<CalendarioMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    VacinaMvpPresenter<VacinaMvpView> provideVacinaPresenter(VacinaPresenter<VacinaMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IdadeMvpPresenter<IdadeMvpView> provideIdadePresenter(IdadePresenter<IdadeMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DoseMvpPresenter<DoseMvpView> provideDosePresenter(DosePresenter<DoseMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ImunizacaoMvpPresenter<ImunizacaoMvpView> provideImunizacaoPresenter(ImunizacaoPresenter<ImunizacaoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MapaMvpPresenter<MapaMvpView> provideMapaPresenter(MapaPresenter<MapaMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ConfiguracaoMvpPresenter<ConfiguracaoMvpView> provideConfiguracaoPresenter(ConfiguracaoPresenter<ConfiguracaoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CuriosidadeMvpPresenter<CuriosidadeMvpView> provideCuriosidadePresenter(CuriosidadePresenter<CuriosidadeMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IntroducaoMvpPresenter<IntroducaoMvpView> provideIntroducaoPresenter(IntroducaoPresenter<IntroducaoMvpView> presenter) {
        return presenter;
    }
}

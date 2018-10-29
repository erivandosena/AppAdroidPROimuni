package br.com.erivando.vacinaskids.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import br.com.erivando.vacinaskids.di.ActivityContext;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpView;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioPresenter;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoMvpView;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoPresenter;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaMvpView;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaPresenter;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpView;
import br.com.erivando.vacinaskids.ui.activity.dose.DosePresenter;
import br.com.erivando.vacinaskids.ui.activity.mapa.MapaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.mapa.MapaMvpView;
import br.com.erivando.vacinaskids.ui.activity.mapa.MapaPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadePresenter;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoPresenter;
import br.com.erivando.vacinaskids.ui.activity.login.LoginMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.login.LoginMvpView;
import br.com.erivando.vacinaskids.ui.activity.login.LoginPresenter;
import br.com.erivando.vacinaskids.ui.activity.main.MainMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.main.MainMvpView;
import br.com.erivando.vacinaskids.ui.activity.main.MainPresenter;
import br.com.erivando.vacinaskids.ui.activity.splash.SplashMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.splash.SplashMvpView;
import br.com.erivando.vacinaskids.ui.activity.splash.SplashPresenter;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioMvpView;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaPresenter;
import br.com.erivando.vacinaskids.ui.fragment.sobre.SobreMvpPresenter;
import br.com.erivando.vacinaskids.ui.fragment.sobre.SobreMvpView;
import br.com.erivando.vacinaskids.ui.fragment.sobre.SobrePresenter;
import br.com.erivando.vacinaskids.util.rx.AppSchedulerProvider;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

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
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
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

}

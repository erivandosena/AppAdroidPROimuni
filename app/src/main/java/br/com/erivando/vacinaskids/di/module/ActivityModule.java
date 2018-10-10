package br.com.erivando.vacinaskids.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import br.com.erivando.vacinaskids.di.ActivityContext;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoMvpView;
import br.com.erivando.vacinaskids.ui.acoes.cartao.CartaoPresenter;
import br.com.erivando.vacinaskids.ui.acoes.calendario.CalendarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.calendario.CalendarioMvpView;
import br.com.erivando.vacinaskids.ui.acoes.calendario.CalendarioPresenter;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaMvpView;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaPresenter;
import br.com.erivando.vacinaskids.ui.acoes.dose.DoseMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.dose.DoseMvpView;
import br.com.erivando.vacinaskids.ui.acoes.dose.DosePresenter;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadePresenter;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioMvpView;
import br.com.erivando.vacinaskids.ui.acoes.usuario.CadastroUsuarioPresenter;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaPresenter;
import br.com.erivando.vacinaskids.ui.login.LoginMvpPresenter;
import br.com.erivando.vacinaskids.ui.login.LoginMvpView;
import br.com.erivando.vacinaskids.ui.login.LoginPresenter;
import br.com.erivando.vacinaskids.ui.main.MainMvpPresenter;
import br.com.erivando.vacinaskids.ui.main.MainMvpView;
import br.com.erivando.vacinaskids.ui.main.MainPresenter;
import br.com.erivando.vacinaskids.ui.sobre.SobreMvpPresenter;
import br.com.erivando.vacinaskids.ui.sobre.SobreMvpView;
import br.com.erivando.vacinaskids.ui.sobre.SobrePresenter;
import br.com.erivando.vacinaskids.ui.splash.SplashMvpPresenter;
import br.com.erivando.vacinaskids.ui.splash.SplashMvpView;
import br.com.erivando.vacinaskids.ui.splash.SplashPresenter;
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
}

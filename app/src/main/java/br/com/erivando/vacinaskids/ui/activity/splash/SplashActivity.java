package br.com.erivando.vacinaskids.ui.activity.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.broadcast.AlarmNotificationReceiver;
import br.com.erivando.vacinaskids.broadcast.LocalData;
import br.com.erivando.vacinaskids.broadcast.NotificationScheduler;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.notification.NotificationHelper;
import br.com.erivando.vacinaskids.service.Servico;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.vacinaskids.ui.activity.login.LoginActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import butterknife.ButterKnife;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 22:17
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;

    private LocalData localData;
    private int hour;
    private int min;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(SplashActivity.this);
    }

    /**
     * Fazendo a tela aguardar para que a logomar seja mostrada
     */
    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void startServico() {
      //  Intent intent = new Intent(SplashActivity.this, Servico.class);
      //  startService(intent);

        localData = new LocalData(getApplicationContext());
        hour = localData.get_hour();
        min = localData.get_min();

       // NotificationScheduler.setReminder(SplashActivity.this, AlarmNotificationReceiver.class, localData.get_hour(), localData.get_min());
       // NotificationHelper.scheduleRepeatingRTCNotification(SplashActivity.this, String.valueOf(localData.get_hour()), String.valueOf(localData.get_min()));

        NotificationHelper.scheduleRepeatingElapsedNotification(SplashActivity.this);
        NotificationHelper.enableBootReceiver(SplashActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        if (android.os.Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        habilitaTelaCheia(this);
    }

    @Override
    public Context getContextActivity() {
        return SplashActivity.this;
    }

}
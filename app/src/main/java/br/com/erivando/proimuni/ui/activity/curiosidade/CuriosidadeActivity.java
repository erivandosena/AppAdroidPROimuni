package br.com.erivando.proimuni.ui.activity.curiosidade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Janeiro de 2019 as 21:17
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CuriosidadeActivity extends BaseActivity implements CuriosidadeMvpView {

    @Inject
    CuriosidadeMvpPresenter<CuriosidadeMvpView> presenter;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CalendarioActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curiosidade);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        textViewTituloToobar.setText("Curiosidades");
        setUp();
    }

    @Override
    protected void setUp() {
        resizeCustomizedToobar(linearLayoutToobar);
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    private void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public Context getContextActivity() {
        return CuriosidadeActivity.this;
    }
}
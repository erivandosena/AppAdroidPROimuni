package br.com.erivando.proimuni.ui.activity.curiosidade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
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

    @BindView(R.id.card_view_p1)
    CardView cardViewP1;

    @BindView(R.id.image_seta_p1)
    ImageView imageViewSetaP1;

    @BindView(R.id.titulo_p1)
    TextView textViewP1;

    @BindView(R.id.desc_p1)
    TextView textViewDescP1;

    @BindView(R.id.card_view_p2)
    CardView cardViewP2;

    @BindView(R.id.image_seta_p2)
    ImageView imageViewSetaP2;

    @BindView(R.id.titulo_p2)
    TextView textViewP2;

    @BindView(R.id.desc_p2)
    TextView textViewDescP2;

    @BindView(R.id.card_view_p3)
    CardView cardViewP3;

    @BindView(R.id.image_seta_p3)
    ImageView imageViewSetaP3;

    @BindView(R.id.titulo_p3)
    TextView textViewP3;

    @BindView(R.id.desc_p3)
    TextView textViewDescP3;

    @BindView(R.id.card_view_p4)
    CardView cardViewP4;

    @BindView(R.id.image_seta_p4)
    ImageView imageViewSetaP4;

    @BindView(R.id.titulo_p4)
    TextView textViewP4;

    @BindView(R.id.desc_p4)
    TextView textViewDescP4;

    @BindView(R.id.card_view_p5)
    CardView cardViewP5;

    @BindView(R.id.image_seta_p5)
    ImageView imageViewSetaP5;

    @BindView(R.id.titulo_p5)
    TextView textViewP5;

    @BindView(R.id.desc_p5)
    TextView textViewDescP5;

    @BindView(R.id.card_view_p6)
    CardView cardViewP6;

    @BindView(R.id.image_seta_p6)
    ImageView imageViewSetaP6;

    @BindView(R.id.titulo_p6)
    TextView textViewP6;

    @BindView(R.id.desc_p6)
    TextView textViewDescP6;

    @BindView(R.id.card_view_p7)
    CardView cardViewP7;

    @BindView(R.id.image_seta_p7)
    ImageView imageViewSetaP7;

    @BindView(R.id.titulo_p7)
    TextView textViewP7;

    @BindView(R.id.desc_p7)
    TextView textViewDescP7;

    @BindView(R.id.card_view_p8)
    CardView cardViewP8;

    @BindView(R.id.image_seta_p8)
    ImageView imageViewSetaP8;

    @BindView(R.id.titulo_p8)
    TextView textViewP8;

    @BindView(R.id.desc_p8)
    TextView textViewDescP8;

    @BindView(R.id.card_view_p9)
    CardView cardViewP9;

    @BindView(R.id.image_seta_p9)
    ImageView imageViewSetaP9;

    @BindView(R.id.titulo_p9)
    TextView textViewP9;

    @BindView(R.id.desc_p9)
    TextView textViewDescP9;

    private float anguloNormal = 0;
    private float anguloGiro = 180f;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CuriosidadeActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curiosidade);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        textViewTituloToobar.setText(getResources().getString(R.string.menu_curiosidade));

        textViewP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP1.getVisibility() == View.GONE) {
                    textViewDescP1.setVisibility(View.VISIBLE);
                    imageViewSetaP1.setRotation(anguloGiro);
                } else {
                    textViewDescP1.setVisibility(View.GONE);
                    imageViewSetaP1.setRotation(anguloNormal);
                }
            }
        });
        cardViewP1.setRadius(20f);

        textViewP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP2.getVisibility() == View.GONE) {
                    textViewDescP2.setVisibility(View.VISIBLE);
                    imageViewSetaP2.setRotation(anguloGiro);
                } else {
                    textViewDescP2.setVisibility(View.GONE);
                    imageViewSetaP2.setRotation(anguloNormal);
                }
            }
        });
        cardViewP2.setRadius(20f);

        textViewP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP3.getVisibility() == View.GONE) {
                    textViewDescP3.setVisibility(View.VISIBLE);
                    imageViewSetaP3.setRotation(anguloGiro);
                } else {
                    textViewDescP3.setVisibility(View.GONE);
                    imageViewSetaP3.setRotation(anguloNormal);
                }
            }
        });
        cardViewP3.setRadius(20f);

        textViewP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP4.getVisibility() == View.GONE) {
                    textViewDescP4.setVisibility(View.VISIBLE);
                    imageViewSetaP4.setRotation(anguloGiro);
                } else {
                    textViewDescP4.setVisibility(View.GONE);
                    imageViewSetaP4.setRotation(anguloNormal);
                }
            }
        });
        cardViewP4.setRadius(20f);

        textViewP5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP5.getVisibility() == View.GONE) {
                    textViewDescP5.setVisibility(View.VISIBLE);
                    imageViewSetaP5.setRotation(anguloGiro);
                } else {
                    textViewDescP5.setVisibility(View.GONE);
                    imageViewSetaP5.setRotation(anguloNormal);
                }
            }
        });
        cardViewP5.setRadius(20f);

        textViewP6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP6.getVisibility() == View.GONE) {
                    textViewDescP6.setVisibility(View.VISIBLE);
                    imageViewSetaP6.setRotation(anguloGiro);
                } else {
                    textViewDescP6.setVisibility(View.GONE);
                    imageViewSetaP6.setRotation(anguloNormal);
                }
            }
        });
        cardViewP6.setRadius(20f);

        textViewP7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP7.getVisibility() == View.GONE) {
                    textViewDescP7.setVisibility(View.VISIBLE);
                    imageViewSetaP7.setRotation(anguloGiro);
                } else {
                    textViewDescP7.setVisibility(View.GONE);
                    imageViewSetaP7.setRotation(anguloNormal);
                }
            }
        });
        cardViewP7.setRadius(20f);

        textViewP8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP8.getVisibility() == View.GONE) {
                    textViewDescP8.setVisibility(View.VISIBLE);
                    imageViewSetaP8.setRotation(anguloGiro);
                } else {
                    textViewDescP8.setVisibility(View.GONE);
                    imageViewSetaP8.setRotation(anguloNormal);
                }
            }
        });
        cardViewP8.setRadius(20f);

        textViewP9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescP9.getVisibility() == View.GONE) {
                    textViewDescP9.setVisibility(View.VISIBLE);
                    imageViewSetaP9.setRotation(anguloGiro);
                } else {
                    textViewDescP9.setVisibility(View.GONE);
                    imageViewSetaP9.setRotation(anguloNormal);
                }
            }
        });
        cardViewP9.setRadius(20f);

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
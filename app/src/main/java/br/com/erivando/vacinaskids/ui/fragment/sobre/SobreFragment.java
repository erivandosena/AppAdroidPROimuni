package br.com.erivando.vacinaskids.ui.fragment.sobre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.BuildConfig;
import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.di.component.ActivityComponent;
import br.com.erivando.vacinaskids.mvp.base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Julho de 2018 as 11:57
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class SobreFragment extends BaseFragment implements SobreMvpView {

    public static final String TAG = "AboutFragment";

    @Inject
    SobreMvpPresenter<SobreMvpView> presenter;

    @BindView(R.id.text_sobre_versao_app)
    TextView versaoAppSobreTextView;

    public static SobreFragment newInstance() {
        Bundle args = new Bundle();
        SobreFragment fragment = new SobreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sobre, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            presenter.onAttach(this);
        }

        return view;
    }

    @Override
    protected void setUp(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @OnClick(R.id.btn_nav_voltar)
    void onNavBackClick() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.versao_app) + ": " + BuildConfig.VERSION_NAME;
        versaoAppSobreTextView.setText(version);
    }

}

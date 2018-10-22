package br.com.erivando.vacinaskids.ui.fragment.mapa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.di.component.ActivityComponent;
import br.com.erivando.vacinaskids.mvp.base.BaseFragment;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Outubro de 2018 as 10:34
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class MapaFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_sobre, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
           // component.inject(this);
          //  setUnBinder(ButterKnife.bind(this, rv));
           // presenter.onAttach(this);
        }
        //setupRecyclerView(rv);

        return rv;

    }

    @Override
    protected void setUp(View view) {

    }

    @Override
    public Context getContextActivity() {
        return getContext();
    }
}

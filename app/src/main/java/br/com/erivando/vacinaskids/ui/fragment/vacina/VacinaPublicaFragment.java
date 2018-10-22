
package br.com.erivando.vacinaskids.ui.fragment.vacina;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.di.component.ActivityComponent;
import br.com.erivando.vacinaskids.mvp.base.BaseFragment;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 13:14
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaPublicaFragment extends BaseFragment implements VacinaMvpView {

    @Inject
    VacinaMvpPresenter<VacinaMvpView> presenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.vacina_lista_recyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_vacina_lista, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rv));
            presenter.onAttach(this);
        }
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mLayoutManager = new GridLayoutManager(recyclerView.getContext(), 2);
        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new VacinaRecyclerViewAdapter(getActivity(), presenter.onVacinasPorRede(new String[]{"vaciRede", "Pública"})));
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
    }

    @Override
    public Context getContextActivity() {
        return getContext();
    }
}

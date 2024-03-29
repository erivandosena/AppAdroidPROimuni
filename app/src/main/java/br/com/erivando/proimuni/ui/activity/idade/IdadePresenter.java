package br.com.erivando.proimuni.ui.activity.idade;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:58
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class IdadePresenter<V extends IdadeMvpView> extends BasePresenter<V> implements IdadeMvpPresenter<V> {

    @Inject
    public IdadePresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public List<Idade> onIdadesCadastradas() {
        return getIDataManager().obtemIdades();
    }
}

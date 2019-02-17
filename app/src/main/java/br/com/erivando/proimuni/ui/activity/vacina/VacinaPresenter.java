package br.com.erivando.proimuni.ui.activity.vacina;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaPresenter<V extends VacinaMvpView> extends BasePresenter<V> implements VacinaMvpPresenter<V> {

    @Inject
    public VacinaPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public Vacina onVacinaCadastrada(Long id) {
        return getIDataManager().obtemVacina(id);
    }

    @Override
    public List<Vacina> onVacinasCadastradas() {
        return getIDataManager().obtemVacinasOrderBy("id");
    }

    @Override
    public List<Vacina> onVacinasPorRede(String[] valores) {
        return getIDataManager().obtemVacinas(valores);
    }
}

package br.com.erivando.proimuni.ui.activity.dose;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:02
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class DosePresenter<V extends DoseMvpView> extends BasePresenter<V> implements DoseMvpPresenter<V> {

    @Inject
    public DosePresenter(IDataManager iDataManager) {
        super(iDataManager);
    }


    @Override
    public List<Dose> onDosesCadastradas() {
        return getIDataManager().obtemDoses();
    }
}

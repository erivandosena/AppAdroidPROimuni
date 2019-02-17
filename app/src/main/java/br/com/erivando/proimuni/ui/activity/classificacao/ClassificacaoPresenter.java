package br.com.erivando.proimuni.ui.activity.classificacao;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ClassificacaoPresenter<V extends ClassificacaoMvpView> extends BasePresenter<V> implements ClassificacaoMvpPresenter<V> {
    public ClassificacaoPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }
}

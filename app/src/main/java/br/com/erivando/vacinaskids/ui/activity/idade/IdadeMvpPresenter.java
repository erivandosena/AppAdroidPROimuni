package br.com.erivando.vacinaskids.ui.activity.idade;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:58
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface IdadeMvpPresenter<V extends IdadeMvpView> extends MvpPresenter<V> {

    List<Idade> onIdadesCadastradas();

}

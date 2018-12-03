package br.com.erivando.proimuni.ui.activity.idade;

import java.util.List;

import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

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

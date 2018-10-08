package br.com.erivando.vacinaskids.ui.acoes.vacina;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface VacinaMvpPresenter<V extends VacinaMvpView> extends MvpPresenter<V> {

    List<Vacina> onVacinasCadastradas();

}

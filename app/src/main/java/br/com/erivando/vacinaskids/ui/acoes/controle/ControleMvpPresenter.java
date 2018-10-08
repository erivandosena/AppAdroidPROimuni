package br.com.erivando.vacinaskids.ui.acoes.controle;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Controle;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 00:49
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface ControleMvpPresenter<V extends ControleMvpView> extends MvpPresenter<V> {

    List<Controle> onControlesCadastrados();

    Controle onControleCadastrado();

}

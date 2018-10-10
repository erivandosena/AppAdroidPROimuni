package br.com.erivando.vacinaskids.ui.acoes.calendario;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Calendario;
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
public interface CalendarioMvpPresenter<V extends CalendarioMvpView> extends MvpPresenter<V> {

    List<Calendario> onCalendariosCadastrados();

    Calendario onCalendarioCadastrado();

}

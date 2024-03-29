package br.com.erivando.proimuni.ui.activity.calendario;

import java.util.List;

import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

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

    List<Calendario> onCalendariosPorVacina(String[] valores);

    List<Calendario> onCalendariosPorNomeVacina(String[] valores);

    Calendario onCalendarioCadastrado();

}

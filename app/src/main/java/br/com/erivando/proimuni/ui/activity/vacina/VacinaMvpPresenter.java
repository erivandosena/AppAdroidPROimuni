package br.com.erivando.proimuni.ui.activity.vacina;

import java.util.List;

import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:56
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface VacinaMvpPresenter<V extends VacinaMvpView> extends MvpPresenter<V> {

    Vacina onVacinaCadastrada(Long id);

    List<Vacina> onVacinasCadastradas();

    List<Vacina> onVacinasPorRede(String[] valores);

}

package br.com.erivando.proimuni.ui.activity.dose;

import java.util.List;

import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:01
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface DoseMvpPresenter<V extends DoseMvpView> extends MvpPresenter<V> {

    List<Dose> onDosesCadastradas();
}

package br.com.erivando.vacinaskids.ui.activity.dose;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

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

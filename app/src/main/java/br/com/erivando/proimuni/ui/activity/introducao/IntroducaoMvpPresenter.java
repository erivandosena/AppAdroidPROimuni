package br.com.erivando.proimuni.ui.activity.introducao;

import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Janeiro de 2019 as 01:12
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface IntroducaoMvpPresenter<V extends IntroducaoMvpView> extends MvpPresenter<V> {

    boolean onIsIntroLaunch();

    void onSetIntroLaunch(boolean isFirst);
}

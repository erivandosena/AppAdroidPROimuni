package br.com.erivando.vacinaskids.ui.main;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 18:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void onDrawerOptionAboutClick();

    void onDrawerOptionLogoutClick();

    void onDrawerRateUsClick();

    //void onDrawerMyFeedClick();

    //void onViewInitialized();

    //void onCardExhausted();

    void onNavMenuCreated();
}

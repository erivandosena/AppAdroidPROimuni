package br.com.erivando.proimuni.mvp;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 16:32
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

/**
 * Cada apresentador no aplicativo deve implementar essa interface ou estender o
 * BasePresenter indicando o tipo de MvpView com o qual deseja se conectar.
 */
public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();
}

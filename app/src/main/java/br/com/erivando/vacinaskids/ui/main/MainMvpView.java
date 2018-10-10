package br.com.erivando.vacinaskids.ui.main;

import br.com.erivando.vacinaskids.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 18:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface MainMvpView extends MvpView {
    void showAboutFragment();

    void openLoginActivity();

    void openCadastroUsuarioActivity();

    void openCalendarioVacinal();

    void openCartaoActivity();

    void openCriancaActivity();

    void updateUserName(String currentUserName);

    void updateUserEmail(String currentUserEmail);

    void updateUserProfilePic(String currentUserProfilePicUrl);

    void updateAppVersion();

    void showRateUsDialog();

    void closeNavigationDrawer();

    void lockDrawer();

    void unlockDrawer();

    void setupNavMenu();

    void setupCardContainerView();

    void onFacebookSignOut();

    void onGooleSignOut();

    void onCompartilhaApp();
}


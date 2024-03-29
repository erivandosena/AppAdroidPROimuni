package br.com.erivando.proimuni.ui.activity.main;

import br.com.erivando.proimuni.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 18:00
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface MainMvpView extends MvpView {

    void openLoginActivity();

    void openVacinaActivity();

    void openCadastroUsuarioActivity();

    void openCalendarioVacinal();

    void openCartaoListaActivity(String acao);

    void openCriancaListaActivity(String acao);

    void openSobreActivity();

    void updateUserName(String currentUserName);

    void updateUserEmail(String currentUserEmail);

    void updateUserProfilePic(String currentUserProfilePicUrl);

    void updateAppVersion();

    void showRateUsDialog();

    void closeNavigationDrawer();

    void lockDrawer();

    void unlockDrawer();

    void setupNavMenu();

    void onFacebookSignOut();

    void onGooleSignOut();

    void onCompartilhaApp();

    void onInicializacoes();

}


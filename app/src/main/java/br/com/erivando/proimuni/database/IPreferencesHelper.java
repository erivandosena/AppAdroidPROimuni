package br.com.erivando.proimuni.database;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Julho de 2018 as 22:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface IPreferencesHelper {

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    Long getCurrentUserId();

    void setCurrentUserId(Long userId);

    String getCurrentUserName();

    void setCurrentUserName(String userName);

    String getCurrentUserLogin();

    void setCurrentUserLogin(String userLogin);

    String getCurrentUserEmail();

    void setCurrentUserEmail(String email);

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);

    String getAccessToken();

    void setAccessToken(String accessToken);

    boolean isNotificacoes();

    void setNotificacoes(boolean valor);

    boolean isRedeVacinas();

    void setRedeVacinas(boolean valor);

    void setFirstLaunch(boolean isFirstTime);

    boolean isFirstLaunch();

}

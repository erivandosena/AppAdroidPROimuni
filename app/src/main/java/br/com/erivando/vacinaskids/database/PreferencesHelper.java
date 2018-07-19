package br.com.erivando.vacinaskids.database;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.PreferenceInfo;
import br.com.erivando.vacinaskids.util.AppConstants;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   15 de Julho de 2018 as 00:46
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class PreferencesHelper {

    private final String NOME = "usua_nome";
    private final String LOGIN = "usua_login";
    private final String EMAIL = "usua_email";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(@ApplicationContext Context contextApplication, @PreferenceInfo String prefFileName) {
        sharedPreferences = contextApplication.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public String getNomeUsuarioAtual() {
        return sharedPreferences.getString(NOME, "Erivando Sena");
    }

    public String getLoginUsuarioAtual() {
        return sharedPreferences.getString(LOGIN, "erivandosena");
    }

    public String getEmailUsuarioAtual() {
        return sharedPreferences.getString(EMAIL, "erivandosena@gmail.com");
    }

    public String setNomeUsuarioAtual(String nome) {
        return sharedPreferences.getString(NOME, nome);
    }

    public String setLoginUsuarioAtual(String nome) {
        return sharedPreferences.getString(LOGIN, nome);
    }

    public String setEmailUsuarioAtual(String email) {
        return sharedPreferences.getString(EMAIL, email);
    }

    public void alteraUsuarioAtual(String nome, String login, String email) {
        getEditor().putString(NOME, nome).apply();
        getEditor().putString(LOGIN, login).apply();
        getEditor().putString(EMAIL, email).apply();
    }

    public void limpaUsuarioAtual() {
        getEditor().putString(NOME, "null").apply();
        getEditor().putString(LOGIN, "null").apply();
        getEditor().putString(EMAIL, "null").apply();
    }

    public boolean getLoggedInMode() {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    public void setLoggedInMode(boolean loggedIn) {
        sharedPreferences.edit().putBoolean("IS_LOGGED_IN", loggedIn).apply();
    }

    public int getCurrentUserLoggedInMode() {
        return sharedPreferences.getInt("PREF_KEY_USER_LOGGED_IN_MODE",
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        sharedPreferences.edit().putInt("PREF_KEY_USER_LOGGED_IN_MODE", mode.getType()).apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString("PREF_KEY_ACCESS_TOKEN", null);
    }

    public void setAccessToken(String accessToken) {
        sharedPreferences.edit().putString("PREF_KEY_ACCESS_TOKEN", accessToken).apply();
    }

    public String getCurrentUserProfilePicUrl() {
        return sharedPreferences.getString("PREF_KEY_CURRENT_USER_PROFILE_PIC_URL", null);
    }

    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        sharedPreferences.edit().putString("PREF_KEY_CURRENT_USER_PROFILE_PIC_URL", profilePicUrl).apply();
    }

    public Long getCurrentUserId() {
        long userId = sharedPreferences.getLong("PREF_KEY_CURRENT_USER_ID", AppConstants.NULL_INDEX);
        return userId == AppConstants.NULL_INDEX ? null : userId;
    }

    public void setCurrentUserId(Long userId) {
        long id = userId == null ? AppConstants.NULL_INDEX : userId;
        sharedPreferences.edit().putLong("PREF_KEY_CURRENT_USER_ID", id).apply();
    }
}

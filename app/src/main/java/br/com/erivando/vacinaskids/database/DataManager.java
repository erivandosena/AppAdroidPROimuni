package br.com.erivando.vacinaskids.database;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.di.ApplicationContext;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 12:22
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class DataManager implements Manager {

    private final SqliteController controller;
    private final SqlitePreferencesHelper preferencesHelper;
    private final Context context;

    @Inject
    public DataManager(@ApplicationContext Context context, SqliteController controller, SqlitePreferencesHelper preferencesHelper) {
        this.context = context;
        this.controller = controller;
        this.preferencesHelper = preferencesHelper;
    }

    /* CONTROLLER */
    public boolean novoUsuario(Usuario usuario) throws Exception {
        return controller.adicionaUsuario(usuario);
    }

    public int atualizaUsuario(Usuario usuario, Integer id) {
        return controller.editaUsuario(usuario, id);
    }

    public boolean eliminaUsuario(Integer id) {
        return controller.excluiUsuario(id);
    }

    public Usuario obtemUsuario(Integer id) throws Resources.NotFoundException, NullPointerException {
        return controller.obtemDadosUsuario(id);
    }

    public boolean validaUsuario(String email, String password) {
        return controller.validaDadosUsuario(email, password);
    }

    public List<Usuario> getAllUsers() {
        return null;
    }

    /* PREFERENCES HELPER */
    public String getAccessToken() {
        return preferencesHelper.getAccessToken();
    }

    public void setAccessToken(String accessToken) {
        preferencesHelper.setAccessToken(accessToken);
    }

    public int getCurrentUserLoggedInMode() {
        return preferencesHelper.getCurrentUserLoggedInMode();
    }

    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        preferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    public Long getCurrentUserId() {
        return preferencesHelper.getCurrentUserId();
    }

    public void setCurrentUserId(Long userId) {
        preferencesHelper.setCurrentUserId(userId);
    }

    public String getCurrentUserName() {
        return preferencesHelper.getNomeUsuarioAtual();
    }

    public void setCurrentUserName(String userName) {
        preferencesHelper.setNomeUsuarioAtual(userName);
    }

    public String getCurrentUserEmail() {
        return preferencesHelper.getEmailUsuarioAtual();
    }

    public void setCurrentUserEmail(String email) {
        preferencesHelper.setEmailUsuarioAtual(email);
    }

    public String getCurrentUserProfilePicUrl() {
        return preferencesHelper.getCurrentUserProfilePicUrl();
    }

    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        preferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

}

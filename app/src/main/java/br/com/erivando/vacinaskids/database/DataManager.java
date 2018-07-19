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
public class DataManager implements IManager {

    private RealmDataBase realmDataBase;
    private final PreferencesHelper preferencesHelper;
    private final Context context;

    @Inject
    public DataManager(@ApplicationContext Context context, PreferencesHelper preferencesHelper, RealmDataBase realmDataBase) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
        this.realmDataBase = realmDataBase;
        this.realmDataBase.setup(context);
    }

    /* CONTROLLER */
    public boolean novoUsuario(Usuario usuario) throws Exception {
        return realmDataBase.addOrUpdate(usuario);
    }

    public boolean atualizaUsuario(Usuario usuario) {
        return realmDataBase.addOrUpdate(usuario);
    }

    public boolean eliminaUsuario(Long id) {
        return realmDataBase.remove(Usuario.class, "usua_id", id);
    }

    public Usuario obtemUsuario(Long id) throws Resources.NotFoundException, NullPointerException {
        return realmDataBase.getObject(Usuario.class, "usua_id", id);
    }

    public boolean validaUsuario(String login, String senha) {
        return false;
    }

    public List<Usuario> obtemTodosUsuarios(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Usuario.class);
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

package br.com.erivando.vacinaskids.database;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.api.ApiHeader;
import br.com.erivando.vacinaskids.database.api.IApiHelper;
import br.com.erivando.vacinaskids.database.api.LoginRequest;
import br.com.erivando.vacinaskids.database.api.LoginResponse;
import br.com.erivando.vacinaskids.database.api.LogoutResponse;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import io.reactivex.Single;
import io.realm.Realm;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 12:22
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class DataManager implements IDataManager {

    private RealmDataBase realmDataBase;
    private final PreferencesHelper preferencesHelper;
    private final Context context;
    private final IApiHelper iapiHelper;

    @Inject
    public DataManager(@ApplicationContext Context context, PreferencesHelper preferencesHelper, RealmDataBase realmDataBase, IApiHelper iapiHelper) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
        this.realmDataBase = realmDataBase;
        this.realmDataBase.setup(context);
        this.iapiHelper = iapiHelper;
    }

    /* CONTROLLER */
    @Override
    public AtomicInteger getUsuarioID() {
        return realmDataBase.getIdByClassModel(Usuario.class);
    }

    @Override
    public boolean validaLoginUsuario(String login, String senha) {
        return realmDataBase.getLoginLocal(Usuario.class, login, senha);
    }

    @Override
    public boolean novoUsuario(Usuario usuario) {
        return realmDataBase.addOrUpdate(usuario);
    }

    public boolean atualizaUsuario(Usuario usuario) {
        return realmDataBase.addOrUpdate(usuario);
    }

    public boolean eliminaUsuario(Long id) {
        return realmDataBase.remove(Usuario.class, "id", id);
    }

    public Usuario obtemUsuario(Long id) {
        return realmDataBase.getObject(Usuario.class, "id", id);
    }

    @Override
    public Usuario obtemUsuario(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(valoresA, valoresB, Usuario.class);
    }

    public List<Usuario> obtemTodosUsuarios(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Usuario.class);
    }

    /* PREFERENCES HELPER */
    @Override
    public void updateApiHeader(Long userId, String accessToken) {

    }

    @Override
    public void updateUserInfo(String accessToken, Long userId, LoggedInMode loggedInMode, String userName, String email, String profilePicPath) {
            setAccessToken(accessToken);
            setCurrentUserId(userId);
            setCurrentUserLoggedInMode(loggedInMode);
            setCurrentUserName(userName);
            setCurrentUserEmail(email);
            setCurrentUserProfilePicUrl(profilePicPath);
            updateApiHeader(userId, accessToken);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                null,
                null);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return preferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        preferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public Long getCurrentUserId() {
        return null;
    }

    @Override
    public void setCurrentUserId(Long userId) {

    }

    @Override
    public String getCurrentUserName() {
        return preferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        preferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserEmail() {
        return preferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        preferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return null;
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {

    }

    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(String accessToken) {

    }

    /* API HELPER */

    @Override
    public ApiHeader getApiHeader() {
        return iapiHelper.getApiHeader();
    }

    @Override
    public Single<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return null;
    }

    @Override
    public Single<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        return iapiHelper.doFacebookLoginApiCall(request);
    }

    @Override
    public Single<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return null;
    }

    @Override
    public Single<LogoutResponse> doLogoutApiCall() {
        return null;
    }

}

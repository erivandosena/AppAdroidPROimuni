package br.com.erivando.vacinaskids.database;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.erivando.vacinaskids.database.api.IApiHelper;
import br.com.erivando.vacinaskids.database.model.Usuario;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   15 de Julho de 2018 as 11:48
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface IDataManager extends IPreferencesHelper, IApiHelper {
    void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FACEBOOK(2),
        LOGGED_IN_MODE_SERVER(3),
        LOGGED_IN_MODE_LOCAL(4);

        private final int type;

        LoggedInMode(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    AtomicInteger getUsuarioID();

    void updateApiHeader(Long userId, String accessToken);

    void setUserAsLoggedOut();

    boolean novoAtualizaUsuario(Usuario usuario);

    boolean eliminaUsuario(Long id);

    Usuario obtemUsuario(Long id);

    Usuario obtemUsuario();

    Usuario obtemUsuario(String[] valores);

    Usuario obtemUsuario(String[] valoresA, String[] valoresB);

    List<Usuario> obtemTodosUsuarios(String[] campo, String[] valor);

    boolean validaLoginUsuario(String login, String senha);
}

package br.com.erivando.vacinaskids.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Classificacao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.di.ApplicationContext;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 12:22
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class DataManager implements IDataManager {

    private final RealmDataBase realmDataBase;
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
    /* Criança */
    @Override
    public AtomicInteger getCriancaID() {
        return realmDataBase.getIdByClassModel(Crianca.class);
    }

    @Override
    public boolean novoAtualizaCrianca(Crianca crianca) {
        return realmDataBase.addOrUpdate(crianca);
    }

    @Override
    public boolean eliminaCrianca(Long id) {
        return realmDataBase.remove(Crianca.class, "id", id);
    }

    @Override
    public Crianca obtemCrianca(Long id) {
        return realmDataBase.getObject(Crianca.class, "id", id);
    }

    @Override
    public Crianca obtemCrianca() {
        return realmDataBase.getObject(Crianca.class);
    }

    @Override
    public Crianca obtemCrianca(String[] valores) {
        return realmDataBase.getObject(Crianca.class, valores);
    }

    @Override
    public Crianca obtemCrianca(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Crianca.class, valoresA, valoresB);
    }

    @Override
    public List<Crianca> obtemTodasCriancas(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Crianca.class);
    }

    @Override
    public List<Crianca> obtemTodasCriancas() {
        return realmDataBase.findAll(Crianca.class);
    }

    /* Cartão */
    @Override
    public AtomicInteger getCartaoID() {
        return realmDataBase.getIdByClassModel(Cartao.class);
    }

    @Override
    public boolean novoAtualizaCartao(Cartao cartao) {
        return realmDataBase.addOrUpdate(cartao);
    }

    @Override
    public boolean eliminaCartao(Long id) {
        return realmDataBase.remove(Cartao.class, "id", id);
    }

    @Override
    public Cartao obtemCartao(Long id) {
        return realmDataBase.getObject(Cartao.class, "id", id);
    }

    @Override
    public Cartao obtemCartao() {
        return realmDataBase.getObject(Cartao.class);
    }

    @Override
    public Cartao obtemCartao(String[] valores) {
        return realmDataBase.getObject(Cartao.class, valores);
    }

    @Override
    public Cartao obtemCartao(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Cartao.class, valoresA, valoresB);
    }

    @Override
    public List<Cartao> obtemTodosCartoes(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Cartao.class);
    }

    /* Classificação */
    @Override
    public AtomicInteger getClassificacaoID() {
        return realmDataBase.getIdByClassModel(Classificacao.class);
    }

    @Override
    public boolean novoAtualizaClassificacao(Classificacao classificacao) {
        return realmDataBase.addOrUpdate(classificacao);
    }

    @Override
    public boolean eliminaClassificacao(Long id) {
        return realmDataBase.remove(Classificacao.class, "id", id);
    }

    @Override
    public Classificacao obtemClassificacao(Long id) {
        return realmDataBase.getObject(Classificacao.class, "id", id);
    }

    @Override
    public Classificacao obtemClassificacao() {
        return realmDataBase.getObject(Classificacao.class);
    }

    @Override
    public Classificacao obtemClassificacao(String[] valores) {
        return realmDataBase.getObject(Classificacao.class, valores);
    }

    @Override
    public Classificacao obtemClassificacao(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Classificacao.class, valoresA, valoresB);
    }

    @Override
    public List<Classificacao> obtemTodasClassificacoes(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Classificacao.class);
    }

    /* Dose */
    @Override
    public AtomicInteger getDoseID() {
        return realmDataBase.getIdByClassModel(Dose.class);
    }

    @Override
    public boolean novoAtualizaDose(Dose dose) {
        return realmDataBase.addOrUpdate(dose);
    }

    @Override
    public boolean eliminaDose(Long id) {
        return realmDataBase.remove(Dose.class, "id", id);
    }

    @Override
    public Dose obtemDose(Long id) {
        return realmDataBase.getObject(Dose.class, "id", id);
    }

    @Override
    public Dose obtemDose() {
        return realmDataBase.getObject(Dose.class);
    }

    @Override
    public Dose obtemDose(String[] valores) {
        return realmDataBase.getObject(Dose.class, valores);
    }

    @Override
    public Dose obtemDose(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Dose.class, valoresA, valoresB);
    }

    @Override
    public List<Dose> obtemTodasDoses(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Dose.class);
    }

    @Override
    public List<Dose> obtemTodasDoses() {
        return realmDataBase.findAll(Dose.class);
    }

    /* Idade */
    @Override
    public AtomicInteger getIdadeID() {
        return realmDataBase.getIdByClassModel(Idade.class);
    }

    @Override
    public boolean novoAtualizaIdade(Idade idade) {
        return realmDataBase.addOrUpdate(idade);
    }

    @Override
    public boolean eliminaIdade(Long id) {
        return realmDataBase.remove(Idade.class, "id", id);
    }

    @Override
    public Idade obtemIdade(Long id) {
        return realmDataBase.getObject(Idade.class, "id", id);
    }

    @Override
    public Idade obtemIdade() {
        return realmDataBase.getObject(Idade.class);
    }

    @Override
    public Idade obtemIdade(String[] valores) {
        return realmDataBase.getObject(Idade.class, valores);
    }

    @Override
    public Idade obtemIdade(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Idade.class, valoresA, valoresB);
    }

    @Override
    public List<Idade> obtemTodasIdades(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Idade.class);
    }

    @Override
    public List<Idade> obtemTodasIdades() {
        return realmDataBase.findAll(Idade.class);
    }

    /* Vacina */
    @Override
    public AtomicInteger getVacinaID() {
        return realmDataBase.getIdByClassModel(Vacina.class);
    }

    @Override
    public boolean novoAtualizaVacina(Vacina vacina) {
        return realmDataBase.addOrUpdate(vacina);
    }

    @Override
    public boolean eliminaVacina(Long id) {
        return realmDataBase.remove(Vacina.class, "id", id);
    }

    @Override
    public Vacina obtemVacina(Long id) {
        return realmDataBase.getObject(Vacina.class, "id", id);
    }

    @Override
    public Vacina obtemVacina() {
        return realmDataBase.getObject(Vacina.class);
    }

    @Override
    public Vacina obtemVacina(String[] valores) {
        return realmDataBase.getObject(Vacina.class, valores);
    }

    @Override
    public Vacina obtemVacina(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Vacina.class, valoresA, valoresB);
    }

    @Override
    public List<Vacina> obtemTodasVacinas(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Vacina.class);
    }

    @Override
    public List<Vacina> obtemTodasVacinas() {
        return realmDataBase.findAll(Vacina.class);
    }

    /* Calendario */
    @Override
    public AtomicInteger getControleID() {
        return realmDataBase.getIdByClassModel(Calendario.class);
    }

    @Override
    public boolean novoAtualizaControle(Calendario calendario) {
        return false;
    }

    @Override
    public boolean eliminaControle(Long id) {
        return false;
    }

    @Override
    public Calendario obtemCalendario(Long id) {
        return null;
    }

    @Override
    public Calendario obtemCalendario() {
        return null;
    }

    @Override
    public Calendario obtemCalendario(String[] valores) {
        return null;
    }

    @Override
    public Calendario obtemCalendario(String[] valoresA, String[] valoresB) {
        return null;
    }

    @Override
    public List<Calendario> obtemTodosCalendarios(String[] campo, String[] valor) {
        return null;
    }

    @Override
    public List<Calendario> obtemTodosCalendarios() {
        return realmDataBase.findAll(Calendario.class);
    }

    /* Usuário */
    @Override
    public AtomicInteger getUsuarioID() {
        return realmDataBase.getIdByClassModel(Usuario.class);
    }

    @Override
    public boolean novoAtualizaUsuario(Usuario usuario) {
        return realmDataBase.addOrUpdate(usuario);
    }

    @Override
    public boolean eliminaUsuario(Long id) {
        return realmDataBase.remove(Usuario.class, "id", id);
    }

    @Override
    public Usuario obtemUsuario(Long id) {
        return realmDataBase.getObject(Usuario.class, "id", id);
    }

    @Override
    public Usuario obtemUsuario() {
        return realmDataBase.getObject(Usuario.class);
    }

    @Override
    public Usuario obtemUsuario(String[] valores) {
        return realmDataBase.getObject(Usuario.class, valores);
    }

    @Override
    public Usuario obtemUsuario(String[] valoresA, String[] valoresB) {
        return realmDataBase.getObject(Usuario.class, valoresA, valoresB);
    }

    @Override
    public List<Usuario> obtemTodosUsuarios(String[] campo, String[] valor) {
        return realmDataBase.findAll(campo, valor, Usuario.class);
    }

    @Override
    public boolean validaLoginUsuario(String login, String senha) {
        return realmDataBase.getLoginLocal(Usuario.class, login, senha);
    }

    /* PREFERENCES HELPER */

    @Override
    public void updateUserInfo(String accessToken, Long userId, LoggedInMode loggedInMode, String userName, String email, String profilePicPath) {
            setAccessToken(accessToken);
            setCurrentUserId(userId);
            setCurrentUserLoggedInMode(loggedInMode);
            setCurrentUserName(userName);
            setCurrentUserEmail(email);
            setCurrentUserProfilePicUrl(profilePicPath);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
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
        return preferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        preferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public String getAccessToken() {
        return preferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        preferencesHelper.setAccessToken(accessToken);
    }

}

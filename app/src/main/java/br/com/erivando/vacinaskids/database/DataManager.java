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
        return realmDataBase.getObject(Crianca.class, valores[0], valores[1]);
    }

    @Override
    public List<Crianca> obtemCriancas() {
        return realmDataBase.findAll(Crianca.class);
    }

    @Override
    public List<Crianca> obtemCriancas(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Crianca.class);
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
    public Cartao obtemCartaoPorCrianca(Long id) {
        return realmDataBase.getObject(Cartao.class, "crianca.id", id);
    }

    @Override
    public Cartao obtemCartao(String[] valores) {
        return realmDataBase.getObject(Cartao.class, valores[0], valores[1]);
    }

    @Override
    public List<Cartao> obtemCartoes() {
        return realmDataBase.findAll(Cartao.class);
    }

    @Override
    public List<Cartao> obtemTodosCartoesPorCrianca(Crianca crianca) {
        return realmDataBase.findAll("crianca", crianca.getId(), Cartao.class);
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
        return realmDataBase.getObject(Classificacao.class, valores[0], valores[1]);
    }


    @Override
    public List<Classificacao> obtemClassificacoes(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Classificacao.class);
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
        return realmDataBase.getObject(Dose.class, valores[0], valores[1]);
    }

    @Override
    public List<Dose> obtemDoses(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Dose.class);
    }

    @Override
    public List<Dose> obtemDoses() {
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
        return realmDataBase.getObject(Idade.class, valores[0], valores[1]);
    }

    @Override
    public List<Idade> obtemIdades(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Idade.class);
    }

    @Override
    public List<Idade> obtemIdades() {
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
        return realmDataBase.getObject(Vacina.class, valores[0], valores[1]);
    }

    @Override
    public List<Vacina> obtemVacinas(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Vacina.class);
    }

    @Override
    public List<Vacina> obtemVacinas() {
        return realmDataBase.findAll(Vacina.class);
    }

    @Override
    public List<Vacina> obtemVacinasOrderBy(String orderBy) {
        return realmDataBase.findAllOrderBy(orderBy, Vacina.class);
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
        return realmDataBase.getObject(Calendario.class, "id", id);
    }

    @Override
    public Calendario obtemCalendarioPorVacina(Long id) {
        return realmDataBase.getObject(Calendario.class, "vacina.id", id);
    }

    @Override
    public Calendario obtemCalendarioPorDose(Long id) {
        return realmDataBase.getObject(Calendario.class, "dose.id", id);
    }

    @Override
    public Calendario obtemCalendarioPorIdade(Long id) {
        return realmDataBase.getObject(Calendario.class, "idade.id", id);
    }

    @Override
    public Calendario obtemCalendario() {
        return realmDataBase.getObject(Calendario.class);
    }

    @Override
    public Calendario obtemCalendario(String[] valores) {
        return realmDataBase.getObject(Calendario.class, valores[0], valores[1]);
    }

    @Override
    public List<Calendario> obtemCalendarios(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Calendario.class);
    }

    @Override
    public List<Calendario> obtemCalendarios() {
        return realmDataBase.findAll(Calendario.class);
    }

    @Override
    public List<Calendario> obtemCalendariosOrderBy(String orderBy) {
        return realmDataBase.findAllOrderBy(orderBy, Calendario.class);
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
    public List<Usuario> obtemUsuarios(String[] valores) {
        return realmDataBase.findAll(valores[0], valores[1], Usuario.class);
    }

    @Override
    public boolean validaLoginUsuario(String login, String senha) {
        return realmDataBase.getLoginLocal(Usuario.class, login, senha);
    }

    /* JSON */
    @Override
    public void getRecursosJson() {
        realmDataBase.getImportJson();
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

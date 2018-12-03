package br.com.erivando.proimuni.database;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Classificacao;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.database.model.Vacina;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   15 de Julho de 2018 as 11:48
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface IDataManager extends IPreferencesHelper {
    void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath);

    /* USUÁRIO */
    AtomicInteger getUsuarioID();

    void setUserAsLoggedOut();

    boolean novoAtualizaUsuario(Usuario usuario);

    boolean eliminaUsuario(Long id);

    Usuario obtemUsuario(Long id);

    Usuario obtemUsuario();

    Usuario obtemUsuario(String[] valores);

    List<Usuario> obtemUsuarios(String[] valores);

    boolean validaLoginUsuario(String login, String senha);

    /* CRIANÇA */
    AtomicInteger getCriancaID();

    boolean novoAtualizaCrianca(Crianca crianca);

    boolean eliminaCrianca(Long id);

    Crianca obtemCrianca(Long id);

    Crianca obtemCrianca();

    Crianca obtemCrianca(String[] valores);

    Crianca obtemCriancaPorUsuario(Long id);

    List<Crianca> obtemCriancas(String[] valores);

    List<Crianca> obtemCriancas();

    /* CARTÃO */
    AtomicInteger getCartaoID();

    boolean novoAtualizaCartao(Cartao cartao);

    boolean eliminaCartao(Long id);

    Cartao obtemCartao(Long id);

    Cartao obtemCartao();

    Cartao obtemCartaoPorCrianca(Long id);

    Cartao obtemCartao(String[] valores);

    List<Cartao> obtemCartoes();

    List<Cartao> obtemTodosCartoesPorCrianca(Crianca crianca);

    /* CLASSIFICAÇÃO */
    AtomicInteger getClassificacaoID();

    boolean novoAtualizaClassificacao(Classificacao classificacao);

    boolean eliminaClassificacao(Long id);

    Classificacao obtemClassificacao(Long id);

    Classificacao obtemClassificacao();

    Classificacao obtemClassificacao(String[] valores);

    List<Classificacao> obtemClassificacoes(String[] valores);

    /* DOSE */
    AtomicInteger getDoseID();

    boolean novoAtualizaDose(Dose dose);

    boolean eliminaDose(Long id);

    Dose obtemDose(Long id);

    Dose obtemDose();

    Dose obtemDose(String[] valores);

    List<Dose> obtemDoses(String[] valores);

    List<Dose> obtemDoses();

    /* IDADE */
    AtomicInteger getIdadeID();

    boolean novoAtualizaIdade(Idade idade);

    boolean eliminaIdade(Long id);

    Idade obtemIdade(Long id);

    Idade obtemIdade();

    Idade obtemIdade(String[] valores);

    List<Idade> obtemIdades(String[] valores);

    List<Idade> obtemIdades();

    /* VACINA */
    AtomicInteger getVacinaID();

    boolean novoAtualizaVacina(Vacina vacina);

    boolean eliminaVacina(Long id);

    Vacina obtemVacina(Long id);

    Vacina obtemVacina();

    Vacina obtemVacina(String[] valores);

    List<Vacina> obtemVacinas(String[] valores);

    List<Vacina> obtemVacinas();

    List<Vacina> obtemVacinasOrderBy(String orderBy);

    /* CALENDÁRIO */
    AtomicInteger getControleID();

    boolean novoAtualizaControle(Calendario calendario);

    boolean eliminaControle(Long id);

    Calendario obtemCalendario();

    Calendario obtemCalendario(Long id);

    Calendario obtemCalendarioPorVacina(Long id);

    Calendario obtemCalendarioPorDose(Long id);

    Calendario obtemCalendarioPorIdade(Long id);

    Calendario obtemCalendario(String[] valores);

    List<Calendario> obtemCalendarios(String[] valores);

    List<Calendario> obtemCalendarios();

    List<Calendario> obtemCalendariosOrderBy(String orderBy);

    /* IMUNIZAÇÃO */
    AtomicInteger getImunizacaoID();

    boolean novoAtualizaImunizacao(Imunizacao imunizacao);

    boolean eliminaImunizacao(Long id);

    Imunizacao obtemImunizacao(Long id);

    Imunizacao obtemImunizacao();

    Imunizacao obtemImunizacao(String[] valores);

    Imunizacao obtemImunizacao(String[] strings, Long[] longs);

    List<Imunizacao> obtemImunizacoes(String[] strings, Long[] longs);

    List<Imunizacao> obtemImunizacoes(String[] valores);

    List<Imunizacao> obtemImunizacoes();

    /* IMPORTAÇÕES */
    void getRecursosJson();

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
}

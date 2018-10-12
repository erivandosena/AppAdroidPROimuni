package br.com.erivando.vacinaskids.database;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Classificacao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.database.model.Vacina;

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

    /* USUÁRIO */
    AtomicInteger getUsuarioID();

    void setUserAsLoggedOut();

    boolean novoAtualizaUsuario(Usuario usuario);

    boolean eliminaUsuario(Long id);

    Usuario obtemUsuario(Long id);

    Usuario obtemUsuario();

    Usuario obtemUsuario(String[] valores);

    Usuario obtemUsuario(String[] valoresA, String[] valoresB);

    List<Usuario> obtemTodosUsuarios(String[] campo, String[] valor);

    boolean validaLoginUsuario(String login, String senha);

    /* CRIANÇA */
    AtomicInteger getCriancaID();

    boolean novoAtualizaCrianca(Crianca crianca);

    boolean eliminaCrianca(Long id);

    Crianca obtemCrianca(Long id);

    Crianca obtemCrianca();

    Crianca obtemCrianca(String[] valores);

    Crianca obtemCrianca(String[] valoresA, String[] valoresB);

    List<Crianca> obtemTodasCriancas(String[] campo, String[] valor);

    List<Crianca> obtemTodasCriancas();

    /* CARTÃO */
    AtomicInteger getCartaoID();

    boolean novoAtualizaCartao(Cartao cartao);

    boolean eliminaCartao(Long id);

    Cartao obtemCartao(Long id);

    Cartao obtemCartao();

    Cartao obtemCartaoPorCrianca(Long id);

    Cartao obtemCartao(String[] valores);

    Cartao obtemCartao(String[] valoresA, String[] valoresB);

    List<Cartao> obtemTodosCartoes();

    List<Cartao> obtemTodosCartoesPorCrianca(Crianca crianca);

    /* CLASSIFICAÇÃO */
    AtomicInteger getClassificacaoID();

    boolean novoAtualizaClassificacao(Classificacao classificacao);

    boolean eliminaClassificacao(Long id);

    Classificacao obtemClassificacao(Long id);

    Classificacao obtemClassificacao();

    Classificacao obtemClassificacao(String[] valores);

    Classificacao obtemClassificacao(String[] valoresA, String[] valoresB);

    List<Classificacao> obtemTodasClassificacoes(String[] campo, String[] valor);

    /* DOSE */
    AtomicInteger getDoseID();

    boolean novoAtualizaDose(Dose dose);

    boolean eliminaDose(Long id);

    Dose obtemDose(Long id);

    Dose obtemDose();

    Dose obtemDose(String[] valores);

    Dose obtemDose(String[] valoresA, String[] valoresB);

    List<Dose> obtemTodasDoses(String[] campo, String[] valor);

    List<Dose> obtemTodasDoses();

    /* IDADE */
    AtomicInteger getIdadeID();

    boolean novoAtualizaIdade(Idade idade);

    boolean eliminaIdade(Long id);

    Idade obtemIdade(Long id);

    Idade obtemIdade();

    Idade obtemIdade(String[] valores);

    Idade obtemIdade(String[] valoresA, String[] valoresB);

    List<Idade> obtemTodasIdades(String[] campo, String[] valor);

    List<Idade> obtemTodasIdades();

    /* VACINA */
    AtomicInteger getVacinaID();

    boolean novoAtualizaVacina(Vacina vacina);

    boolean eliminaVacina(Long id);

    Vacina obtemVacina(Long id);

    Vacina obtemVacina();

    Vacina obtemVacina(String[] valores);

    Vacina obtemVacina(String[] valoresA, String[] valoresB);

    List<Vacina> obtemTodasVacinas(String[] campo, String[] valor);

    List<Vacina> obtemTodasVacinas();

    /* CONTROLE */
    AtomicInteger getControleID();

    boolean novoAtualizaControle(Calendario calendario);

    boolean eliminaControle(Long id);

    Calendario obtemCalendario(Long id);

    Calendario obtemCalendario();

    Calendario obtemCalendario(String[] valores);

    Calendario obtemCalendario(String[] valoresA, String[] valoresB);

    List<Calendario> obtemTodosCalendarios(String[] campo, String[] valor);

    List<Calendario> obtemTodosCalendarios();

}

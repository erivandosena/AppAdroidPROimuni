package br.com.erivando.proimuni.ui.activity.imunizacao;

import br.com.erivando.proimuni.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 16:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public interface ImunizacaoMvpView extends MvpView {

    void openCartaoDetalheActivity(Long idLong);

    void openCartaoListaActivity(String acao);

    void getNomeDose(String nomeDose);
}

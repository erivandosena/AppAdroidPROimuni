package br.com.erivando.vacinaskids.ui.activity.imunizacao;

import br.com.erivando.vacinaskids.database.model.Imunizacao;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 16:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
@PerActivity
public interface ImunizacaoMvpPresenter<V extends ImunizacaoMvpView> extends MvpPresenter<V> {

    void onCadasrarClick(Long id, String imunData, String imunAgente, String imunPosto, String imunLote, Long idVacina, Long idDose, Long idIdade, Long idCartao);

    boolean onNovoAtualizaImunizacao(Imunizacao imunizacao);

    Imunizacao onImunizacaoCadastrada(String[] strings, Long[] longs);

}

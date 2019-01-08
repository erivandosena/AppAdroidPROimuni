package br.com.erivando.proimuni.ui.activity.imunizacao;

import java.util.List;

import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

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

    boolean onRemoveImunizacao(Long id);

    List<Imunizacao> onImunizacoesCadastradas(String[] campo, Long[] id);

    Imunizacao onImunizacaoCadastrada(String[] strings, Long[] longs);

    void onNomeDosePorId(Long id);
}

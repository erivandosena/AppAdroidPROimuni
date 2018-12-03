package br.com.erivando.proimuni.ui.activity.cartao;

import java.util.List;

import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface CartaoMvpPresenter<V extends CartaoMvpView> extends MvpPresenter<V> {

    void onCadasrarClick(Long id, Long idCrianca);

    boolean onNovoAtualizaCartao(Cartao cartao);

    Cartao onCartaoCadastrado(Long id);

    Cartao onCartaoCadastradoPorCrianca(Long id);

    List<Cartao> onCartaoCadastradosPorCrianca(Crianca id);

    List<Cartao> onCartaoCadastrados();
}

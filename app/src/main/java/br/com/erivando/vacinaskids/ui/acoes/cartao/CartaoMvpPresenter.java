package br.com.erivando.vacinaskids.ui.acoes.cartao;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

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

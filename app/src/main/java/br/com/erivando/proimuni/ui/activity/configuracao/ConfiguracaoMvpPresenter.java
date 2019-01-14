package br.com.erivando.proimuni.ui.activity.configuracao;

import android.content.Context;

import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Novembro de 2018 as 11:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


@PerActivity
public interface ConfiguracaoMvpPresenter<V extends ConfiguracaoMvpView> extends MvpPresenter<V> {

    void iniciaServicoNotificacao(Context context);

    void finalizaServicoNotificacao(Context context);

    boolean onNotificacoes();

    void onAtualizaNotificacoes(boolean defValue);

    boolean onRedeVacinas();

    void onAtualizaRedeVacinas(boolean defValue);
}

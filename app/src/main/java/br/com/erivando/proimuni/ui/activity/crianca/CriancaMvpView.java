package br.com.erivando.proimuni.ui.activity.crianca;

import android.content.Intent;

import br.com.erivando.proimuni.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface CriancaMvpView extends MvpView {

    void getStartActivityForResult(Intent intentImagem, int requestImgCamera);

    void openCriancaActivity();

    void openCriancaListaActivity(String acao);

    void openCartaoListaActivity(String acao);
}

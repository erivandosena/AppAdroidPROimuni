package br.com.erivando.proimuni.ui.activity.usuario;

import android.content.Intent;

import br.com.erivando.proimuni.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


public interface CadastroUsuarioMvpView extends MvpView {

    void openLoginOuMainActivity();

    void getStartActivityForResult(Intent intentImagem, int requestImgCamera);
}

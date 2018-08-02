package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.erivando.vacinaskids.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


public interface CadastroUsuarioMvpView extends MvpView {

    void openLoginOuMainActivity();

    void onIncluiFotoUsuario();

    void getStartActivityForResult(Intent intentImagem, int requestImgCamera);
}

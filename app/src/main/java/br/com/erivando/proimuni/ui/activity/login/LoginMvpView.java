package br.com.erivando.proimuni.ui.activity.login;

import android.app.Activity;
import android.content.Intent;

import br.com.erivando.proimuni.mvp.MvpView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Julho de 2018 as 23:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface LoginMvpView extends MvpView {

    void openMainActivity();

    void getStartActivityForResult(Intent intent, int i);

    Activity getContextActivity();
}

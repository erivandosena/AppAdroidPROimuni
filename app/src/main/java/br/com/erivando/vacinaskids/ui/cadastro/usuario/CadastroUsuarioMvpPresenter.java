package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


@PerActivity
public interface CadastroUsuarioMvpPresenter<V extends CadastroUsuarioMvpView> extends MvpPresenter<V> {

    void onCadasrarClick(String nome, String login, String email, String senha, String repeteSenha);

    void onLoginClick();
}

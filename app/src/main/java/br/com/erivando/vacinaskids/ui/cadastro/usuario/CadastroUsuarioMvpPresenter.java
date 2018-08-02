package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageButton;

import java.io.File;

import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;
import br.com.erivando.vacinaskids.ui.AppAplicacao;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


@PerActivity
public interface CadastroUsuarioMvpPresenter<V extends CadastroUsuarioMvpView> extends MvpPresenter<V> {

    void onCadasrarClick(Long id, String nome, String login, String email, String senha, String repeteSenha, Bitmap foto);

    void onLoginClick();

    Usuario onUsuarioCadastrado();

    boolean onNovoAtualizaUsuario(Usuario usuario);

    boolean hasPermissoes(Context context, String... permissions);

    void selecionarImagem(@ApplicationContext final Context context);

    void imagemIntentCamera(@ApplicationContext final Context context);

    void imagemIntentGaleria();

    Bitmap onActivityResult(int requestCode, int resultCode, Intent data, @ApplicationContext final Context context, final ImageButton imageButton);
}

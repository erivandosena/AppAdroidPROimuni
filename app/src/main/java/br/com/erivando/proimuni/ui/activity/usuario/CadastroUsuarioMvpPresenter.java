package br.com.erivando.proimuni.ui.activity.usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageButton;

import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.di.ApplicationContext;
import br.com.erivando.proimuni.di.PerActivity;
import br.com.erivando.proimuni.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


@PerActivity
public interface CadastroUsuarioMvpPresenter<V extends CadastroUsuarioMvpView> extends MvpPresenter<V> {

    String getTokenUsuario();

    void onCadasrarClick(Long id, String nome, String login, String email, String senha, String repeteSenha, Bitmap foto);

    boolean onNovoAtualizaUsuario(Usuario usuario);

    Usuario onUsuarioCadastrado();

    void selecionarImagem(@ApplicationContext final Context context);

    void imagemIntentCamera(@ApplicationContext final Context context);

    void imagemIntentGaleria();

    Bitmap onActivityResult(int requestCode, int resultCode, Intent data, @ApplicationContext final Context context, final ImageButton imageButton);

}

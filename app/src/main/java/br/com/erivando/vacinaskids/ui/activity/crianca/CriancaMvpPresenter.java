package br.com.erivando.vacinaskids.ui.activity.crianca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageButton;

import java.util.List;

import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import br.com.erivando.vacinaskids.di.PerActivity;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@PerActivity
public interface CriancaMvpPresenter<V extends CriancaMvpView> extends MvpPresenter<V> {

    void onCadasrarClick(Long id, String nome, String nascimento, String responsavel, String sexo, Bitmap foto);

    boolean onNovoAtualizaCrianca(Crianca crianca);

    List<Crianca> onCriancaCadastrada();

    Crianca onCriancaCadastrada(Long id);

    void selecionarImagem(@ApplicationContext final Context context);

    void imagemIntentCamera(@ApplicationContext final Context context);

    void imagemIntentGaleria();

    Bitmap onActivityResult(int requestCode, int resultCode, Intent data, @ApplicationContext final Context context, final ImageButton imageButton);
}

package br.com.erivando.vacinaskids.mvp.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 17:45
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
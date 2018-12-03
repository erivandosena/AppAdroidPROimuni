package br.com.erivando.proimuni.ui.fragment.vacina;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.erivando.proimuni.mvp.base.BaseViewHolder;
import br.com.erivando.proimuni.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Outubro de 2018 as 19:34
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ViewHolder extends BaseViewHolder {

    public final View mView;
    public String mBoundStringNome;
    public String mBoundStringDesc;
    public String mBoundStringAdmin;
    @BindView(R.id.avatar)
    public ImageView mImageView;

    @BindView(android.R.id.text1)
    public TextView mTextView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void clear() {
        mImageView.setImageDrawable(null);
        mTextView.setText("");
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextView.getText();
    }
}

package br.com.erivando.proimuni.ui.fragment.vacina;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseViewHolder;
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
    public String mBoundStringRede;
    public String mBoundStringDesc;
    public String mBoundStringAdmin;

    //@BindView(R.id.avatar)
   //public ImageView mImageView;

    @BindView(R.id.card_view_vacina)
    public CardView cardViewColorVacina;

    @BindView(R.id.nome_vacina)
    public TextView mTextViewNome;

    @BindView(R.id.descricao_vacina)
    public TextView mTextViewDescricao;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void clear() {
        //mImageView.setImageDrawable(null);
        mTextViewNome.setText("");
        mTextViewDescricao.setText("");
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextViewNome.getText();
    }
}

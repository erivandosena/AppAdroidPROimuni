package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.imagem.RoundedImageView;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.proimuni.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.erivando.proimuni.util.Uteis.obtemIdadePorDiaOuMesOuAno;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   05 de Novembro de 2018 as 21:12
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoRVA extends RecyclerView.Adapter<CartaoRVA.SingleItemRowHolder>{

    private List<Cartao> cartaoList;
    private Cartao cartao;
    private Context mContext;
    private Intent intent;
    private Context context;

    public CartaoRVA(List<Cartao> cartaoList, Context mContext, Intent intent) {
        this.cartaoList = cartaoList;
        this.mContext = mContext;
        this.intent = intent;
        this.context = mContext;
    }

    @NonNull
    @Override
    public CartaoRVA.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartao_item, null);
        CartaoRVA.SingleItemRowHolder singleItemRowHolder = new CartaoRVA.SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartaoRVA.SingleItemRowHolder holder, int position) {
        cartao = cartaoList.get(position);

        if (cartao.getCrianca() != null && cartao.getCrianca().getCriaFoto() != null)
            holder.imageView.setImageBitmap(Uteis.base64ParaBitmap(cartao.getCrianca().getCriaFoto()));
        else
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_round));

        //holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_gerenciar));
        holder.tvTitle.setText(cartao.getCrianca().getCriaNome());
        holder.tvSubtitle.setText(obtemIdadePorDiaOuMesOuAno(cartao.getCrianca().getCriaNascimento()));
        holder.idCartao = cartao.getId();

        holder.cardViewColorCartao.setCardBackgroundColor(context.getResources().getColor((R.color.colorGreen)));
    }

    @Override
    public int getItemCount() {
        return (null != cartaoList ? cartaoList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected RoundedImageView imageView;
        protected TextView tvTitle;
        protected TextView tvSubtitle;
        protected Long idCartao;

        @BindView(R.id.cardview_cartao)
        protected CardView cardViewColorCartao;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            imageView = view.findViewById(R.id.image_cartao);
            tvTitle = view.findViewById(R.id.text_titulo_cartao);
            tvSubtitle = view.findViewById(R.id.text_sub_titulo_cartao);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CartaoListaActvity)mContext).showLoading();
                    try {
                        if (intent != null) {
                            String acao = intent.getStringExtra("cartaoLista");
                            Intent intencao = null;
                            if ("cartao".equals(acao)) {
                                intencao = CartaoDetalheActivity.getStartIntent(mContext);
                                intencao.putExtra("cartao", idCartao);
                                //((CartaoListaActvity)mContext).finish();
                            }
                            if ("edita".equals(acao)) {
                                intencao = CartaoActivity.getStartIntent(mContext);
                                intencao.putExtra("cartao", idCartao);
                            }
                            if (intencao != null) {
                                mContext.startActivity(intencao);
                                ((CartaoListaActvity)mContext).finish();
                            }
                        }
                    } finally {
                        ((CartaoListaActvity)mContext).hideLoading();
                    }
                }
            });
        }

    }
}

package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.imagem.RoundedImageView;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaImunizacao;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.erivando.proimuni.util.Uteis.obtemIdadeCompleta;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   05 de Novembro de 2018 as 19:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CriancaRVA extends RecyclerView.Adapter<CriancaRVA.SingleItemRowHolder> {

    private List<Crianca> criancaList;
    private List<Cartao> cartaoList;
    private Crianca crianca;
    private Context mContext;
    private Intent intent;
    private Context context;


    public CriancaRVA(List<Crianca> criancaList, List<Cartao> cartaoList, Context mContext, Intent intent) {
        this.criancaList = criancaList;
        this.cartaoList = cartaoList;
        this.mContext = mContext;
        this.intent = intent;
        this.context = mContext;
    }

    @NonNull
    @Override
    public CriancaRVA.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_crianca_item, null);
        CriancaRVA.SingleItemRowHolder singleItemRowHolder = new CriancaRVA.SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CriancaRVA.SingleItemRowHolder holder, int position) {
        crianca = criancaList.get(position);

        holder.tvTitle.setText(crianca.getCriaNome());
        holder.tvSubtitle.setText(obtemIdadeCompleta(crianca.getCriaNascimento()));
        holder.idCrianca =crianca.getId();

        holder.cardViewColorCrianca.setCardBackgroundColor(context.getResources().getColor((R.color.colorPink)));
    }

    @Override
    public int getItemCount() {
        return (null != criancaList ? criancaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected RoundedImageView imageView;
        protected TextView tvTitle;
        protected TextView tvSubtitle;
        protected Long idCrianca;

        @BindView(R.id.cardview_crianca)
        protected CardView cardViewColorCrianca;

        @BindView(R.id.btn_cartao_crianca)
        ImageButton buttonCartaoCrianca;

        @BindView(R.id.btn_imunizacoes)
        ImageButton buttonImunizacoesCrianca;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.imageView = view.findViewById(R.id.image_crianca);
            this.tvTitle = view.findViewById(R.id.text_titulo_crianca);
            this.tvSubtitle = view.findViewById(R.id.text_sub_titulo_crianca);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent != null) {
                        String acao = intent.getStringExtra("criancaLista");
                        Intent intencao = null;
                        if ("edita".equals(acao)) {
                            intencao = CriancaActivity.getStartIntent(mContext);
                            intencao.putExtra("crianca", idCrianca);
                        }
                        if (intencao != null) {
                            mContext.startActivity(intencao);
                            ((CriancaListaActvity)mContext).finish();
                        }
                    }
                }
            });

            buttonCartaoCrianca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Cartao cartao:cartaoList) {
                        if(cartao.getCrianca().getId().equals(idCrianca)) {
                            Intent intent = CartaoDetalheActivity.getStartIntent(mContext);
                            intent.putExtra("cartao", cartao.getId());
                            mContext.startActivity(intent);
                            ((CriancaListaActvity)mContext).finish();
                        }
                    }
                }
            });

            buttonImunizacoesCrianca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Cartao cartao:cartaoList) {
                        if(cartao.getCrianca().getId().equals(idCrianca)) {
                            Intent intent = CriancaListaImunizacao.getStartIntent(mContext);
                            intent.putExtra("cartao", cartao.getId());
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

        }

    }
}

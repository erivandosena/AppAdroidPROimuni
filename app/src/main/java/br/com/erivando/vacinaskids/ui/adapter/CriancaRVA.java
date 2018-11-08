package br.com.erivando.vacinaskids.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.custom.imagem.RoundedImageView;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaActivity;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.util.Uteis;

import static br.com.erivando.vacinaskids.util.Uteis.obtemIdadeCompleta;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   05 de Novembro de 2018 as 19:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CriancaRVA extends RecyclerView.Adapter<CriancaRVA.SingleItemRowHolder> {
    private List<Crianca> criancaList;
    private Crianca crianca;
    private Context mContext;
    private Intent intent;

    public CriancaRVA(List<Crianca> criancaList, Context mContext, Intent intent) {
        this.criancaList = criancaList;
        this.mContext = mContext;
        this.intent = intent;
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

        if (crianca.getCriaFoto() != null)
            holder.imageView.setImageBitmap(Uteis.base64ParaBitmap(crianca.getCriaFoto()));
        else
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_round));
        holder.tvTitle.setText(crianca.getCriaNome());
        holder.tvSubtitle.setText(obtemIdadeCompleta(crianca.getCriaNascimento()));
        holder.idCrianca =crianca.getId();
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

        public SingleItemRowHolder(View view) {
            super(view);

            this.imageView = view.findViewById(R.id.image);
            this.tvTitle = view.findViewById(R.id.text_crianca_tit_vacina);
            this.tvSubtitle = view.findViewById(R.id.text_sub_titulo);

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
        }

    }
}

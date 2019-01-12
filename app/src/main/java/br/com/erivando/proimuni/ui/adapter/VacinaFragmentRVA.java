package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BaseViewHolder;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaDetalheActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Outubro de 2018 as 19:26
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class VacinaFragmentRVA extends RecyclerView.Adapter<VacinaFragmentRVA.ItemViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Vacina> mVacinaValues;
    Context context;

    public VacinaFragmentRVA(Context context, List<Vacina> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mVacinaValues = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vacina_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        holder.mBoundStringNome = mVacinaValues.get(position).getVaciNome();
        holder.mBoundStringRede = mVacinaValues.get(position).getVaciRede();
        holder.mBoundStringDesc = mVacinaValues.get(position).getVaciDescricao();
        holder.mBoundStringAdmin = mVacinaValues.get(position).getVaciAdministracao();
        holder.mTextViewNome.setText(mVacinaValues.get(position).getVaciNome());
        holder.mTextViewDescricao.setText(mVacinaValues.get(position).getVaciDescricao());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, VacinaDetalheActivity.class);
                intent.putExtra(VacinaDetalheActivity.EXTRA_NOME, holder.mBoundStringNome);
                intent.putExtra(VacinaDetalheActivity.EXTRA_REDE, holder.mBoundStringRede);
                intent.putExtra(VacinaDetalheActivity.EXTRA_DESC, holder.mBoundStringDesc);
                intent.putExtra(VacinaDetalheActivity.EXTRA_ADMIN, holder.mBoundStringAdmin);
                context.startActivity(intent);
            }
        });

        if ("Pública".equalsIgnoreCase(mVacinaValues.get(position).getVaciRede()))
            holder.cardViewColorVacina.setCardBackgroundColor(context.getResources().getColor((R.color.colorPink)));

        if ("Privada".equalsIgnoreCase(mVacinaValues.get(position).getVaciRede()))
            holder.cardViewColorVacina.setCardBackgroundColor(context.getResources().getColor((R.color.colorPurple)));
    }

    @Override
    public int getItemCount() {
        return mVacinaValues.size();
    }

    public class ItemViewHolder extends BaseViewHolder {

        public final View mView;
        public String mBoundStringNome;
        public String mBoundStringRede;
        public String mBoundStringDesc;
        public String mBoundStringAdmin;

        @BindView(R.id.card_view_vacina)
        public CardView cardViewColorVacina;

        @BindView(R.id.nome_vacina)
        public TextView mTextViewNome;

        @BindView(R.id.descricao_vacina)
        public TextView mTextViewDescricao;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            mTextViewNome.setText("");
            mTextViewDescricao.setText("");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextViewNome.getText();
        }
    }
}

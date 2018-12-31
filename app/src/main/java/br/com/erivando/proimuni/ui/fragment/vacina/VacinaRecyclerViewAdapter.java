package br.com.erivando.proimuni.ui.fragment.vacina;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaDetalheActivity;
import butterknife.BindView;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   18 de Outubro de 2018 as 19:26
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class VacinaRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Vacina> mVacinaValues;
    Context context;

    public VacinaRecyclerViewAdapter(Context context, List<Vacina> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mVacinaValues = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vacina_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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
}

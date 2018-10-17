package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Vacina;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:15
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaRVAdapter extends RecyclerView.Adapter<VacinaRVAdapter.SingleItemRowHolder> {

    private RealmList<Vacina> vacinaList;
    private Context mContext;

    public VacinaRVAdapter(Context context, RealmList<Vacina> vacinaList) {
        this.vacinaList = vacinaList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_calendario_lista, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        Vacina singleItem = vacinaList.get(i);
        holder.textTitulo.setText(singleItem.getVaciNome());
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView textTitulo;
        protected ImageView imageItem;

        public SingleItemRowHolder(View view) {
            super(view);

            this.textTitulo = (TextView) view.findViewById(R.id.text_titulo);
            this.imageItem = (ImageView) view.findViewById(R.id.image_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), textTitulo.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}

package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Vacina;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CalendarioRVAdapter extends RecyclerView.Adapter<CalendarioRVAdapter.ItemRowHolder> {

    private ArrayList<Calendario> calendarioList;
    private Context mContext;

    public CalendarioRVAdapter(Context context, ArrayList<Calendario> calendarioList) {
        this.calendarioList = calendarioList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_calendario_lista, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = calendarioList.get(i).getHeaderTitulo();
        RealmList<Vacina> singleSectionItems = calendarioList.get(i).getVacinasInSection();
        itemRowHolder.textTitulo.setText(sectionName);
        VacinaRVAdapter itemListDataAdapter = new VacinaRVAdapter(mContext, singleSectionItems);

        itemRowHolder.rvLista.setHasFixedSize(true);
        itemRowHolder.rvLista.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.rvLista.setAdapter(itemListDataAdapter);
        itemRowHolder.rvLista.setNestedScrollingEnabled(false);
        itemRowHolder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), sectionName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != calendarioList ? calendarioList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView textTitulo;
        protected RecyclerView rvLista;
        protected Button btnInfo;

        public ItemRowHolder(View view) {
            super(view);
            this.textTitulo = view.findViewById(R.id.text_titulo);
            this.rvLista = view.findViewById(R.id.recycler_view_lista);
            this.btnInfo = view.findViewById(R.id.btn_info);
        }
    }

}
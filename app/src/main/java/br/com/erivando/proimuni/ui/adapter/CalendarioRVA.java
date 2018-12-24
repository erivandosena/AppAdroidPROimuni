package br.com.erivando.proimuni.ui.adapter;

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

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Vacina;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   29 de Agosto de 2018 as 08:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CalendarioRVA extends RecyclerView.Adapter<CalendarioRVA.ItemRowHolder> {

    private ArrayList<Calendario> calendarioList;
    private Context mContext;

    public CalendarioRVA(ArrayList<Calendario> calendarioList, Context mContext) {
        this.calendarioList = calendarioList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_calendario_lista, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {
        final String sectionName = calendarioList.get(i).getTituloIdade();

        RealmList<Vacina> vacinaSectionItems = calendarioList.get(i).getVacinasInSection();
        RealmList<Dose> doseSectionItems = calendarioList.get(i).getDosesInSection();
        RealmList<Idade> idadeSectionItems = calendarioList.get(i).getIdadesInSection();

        itemRowHolder.textTituloIdade.setText(sectionName);
        CalendarioVacinaRVA itemListDataAdapter = new CalendarioVacinaRVA(vacinaSectionItems, doseSectionItems, idadeSectionItems, mContext);

        itemRowHolder.rvLista.setHasFixedSize(true);
        itemRowHolder.rvLista.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.rvLista.setAdapter(itemListDataAdapter);
        itemRowHolder.rvLista.setNestedScrollingEnabled(false);
        /*
        itemRowHolder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), sectionName, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return (null != calendarioList ? calendarioList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView textTituloIdade;
        protected RecyclerView rvLista;
        protected Button btnInfo;

        public ItemRowHolder(View view) {
            super(view);
            this.textTituloIdade = view.findViewById(R.id.text_calendario_tit_vacina);
            this.rvLista = view.findViewById(R.id.recycler_view_lista);
            //this.btnInfo = view.findViewById(R.id.btn_info);
        }
    }

}
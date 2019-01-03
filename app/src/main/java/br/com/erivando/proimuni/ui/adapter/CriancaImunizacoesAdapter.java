package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Imunizacao;

import static br.com.erivando.proimuni.util.Uteis.getParseDateString;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   30 de Dezembro de 2018 as 13:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CriancaImunizacoesAdapter extends BaseAdapter {

    public LayoutInflater layoutInflater;
    private Context context;
    private List<Imunizacao> lista;

    public CriancaImunizacoesAdapter(Context context,List<Imunizacao> lista) {
        super();
        this.context = context;
        this.lista = lista;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Imunizacao imunizacao = lista.get(position);

        View layout;

        if (convertView == null) {
            layout = layoutInflater.inflate(R.layout.activity_imunizacao_lista, parent, false);
        } else {
            layout = convertView;
        }

        TextView textVacina = layout.findViewById(R.id.text_vacina_nome);
        textVacina.setText(imunizacao.getVacina().getVaciNome());

        TextView textDose = layout.findViewById(R.id.text_dose_descricao);
        textDose.setText(imunizacao.getDose().getDoseDescricao());

        TextView textData = layout.findViewById(R.id.text_imunizacao_data);
        textData.setText(getParseDateString(imunizacao.getImunData()));
        /*
        if (position % 2 == 1) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayLight));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayFineLight));
        }
        */

        return layout;
    }
}
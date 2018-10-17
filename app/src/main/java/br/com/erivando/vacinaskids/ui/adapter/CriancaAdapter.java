package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.custom.imagem.RoundedImageView;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.util.Uteis;

import static br.com.erivando.vacinaskids.util.Uteis.obtemIdadeCompleta;
import static br.com.erivando.vacinaskids.util.Uteis.parseDateString;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Agosto de 2018 as 23:41
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CriancaAdapter extends ArrayAdapter<Crianca> {

    public CriancaAdapter(Context context) {
        super(context, R.layout.activity_crianca_item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.activity_crianca_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Crianca model =  getItem(position);
        if (model.getCriaFoto() != null)
           holder.imageView.setImageBitmap(Uteis.base64ParaBitmap(model.getCriaFoto()));
        else
            holder.imageView.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_launcher_round));
        holder.tvTitle.setText(model.getCriaNome());
        holder.tvSubtitle.setText(obtemIdadeCompleta(model.getCriaNascimento()));//"Nascimento: "+parseDateString(model.getCriaNascimento()));
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView imageView;
        TextView tvTitle;
        TextView tvSubtitle;

        ViewHolder(View view) {
            imageView = (RoundedImageView) view.findViewById(R.id.image);
            tvTitle = (TextView) view.findViewById(R.id.text_titulo);
            tvSubtitle = (TextView) view.findViewById(R.id.text_sub_titulo);
        }
    }
}
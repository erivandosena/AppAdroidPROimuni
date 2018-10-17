package br.com.erivando.vacinaskids.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.custom.imagem.RoundedImageView;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.util.Uteis;

import static br.com.erivando.vacinaskids.util.Uteis.obtemIdadeCompleta;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Outubro de 2018 as 08:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoAdapter extends ArrayAdapter<Cartao> {

    public CartaoAdapter(Context context) {
        super(context, R.layout.activity_cartao_item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.activity_cartao_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cartao model =  getItem(position);
        holder.imageView.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_gerenciar));
        holder.tvTitle.setText(model.getCrianca().getCriaNome());
        holder.tvSubtitle.setText(obtemIdadeCompleta(model.getCrianca().getCriaNascimento()));
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
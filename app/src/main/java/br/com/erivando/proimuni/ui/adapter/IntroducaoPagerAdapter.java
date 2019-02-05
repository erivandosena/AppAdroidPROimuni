package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.erivando.proimuni.R;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Janeiro de 2019 as 02:38
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class IntroducaoPagerAdapter extends PagerAdapter {

    private TextView textTituloSlide1;
    private TextView textDescricaoSlide1;
    private TextView textTituloSlide2;
    private TextView textDescricaoSlide2;
    private TextView textTituloSlide3;
    private TextView textDescricaoSlide3;

    private LayoutInflater layoutInflater;
    private int[] layouts;
    private View view;
    Context context;

    public IntroducaoPagerAdapter(int[] layouts, Context context) {
        this.layouts = layouts;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (position == 0) {
            view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            textTituloSlide1 = (TextView) view.findViewById(R.id.text_titulo_intro_slide_1);
            textTituloSlide1.setText(textTituloSlide1.getText().toString() + " " + context.getString(R.string.app_name));
        }

        if (position == 1) {

            view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            textDescricaoSlide2 = (TextView) view.findViewById(R.id.text_descricao_intro_slide_2);
            textDescricaoSlide2.setText("O " + context.getString(R.string.app_name) + " " + textDescricaoSlide2.getText().toString());
        }

        if (position == 2) {
            view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            textDescricaoSlide3 = (TextView) view.findViewById(R.id.text_descricao_intro_slide_3);
            textDescricaoSlide3.setText("O " + context.getString(R.string.app_name) + " " + textDescricaoSlide3.getText().toString());
        }

        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
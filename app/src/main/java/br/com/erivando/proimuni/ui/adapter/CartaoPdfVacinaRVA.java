package br.com.erivando.proimuni.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

import static br.com.erivando.proimuni.util.Uteis.getParseDateString;

/**
 * Projeto:     PROIMUNI
 * Autor:       Erivando Sena
 * Data/Hora:   10 de Janeiro de 2019 as 01:29
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoPdfVacinaRVA extends RecyclerView.Adapter<CartaoPdfVacinaRVA.SingleItemRowHolder> {

    private RealmList<Vacina> vacinaList;
    private RealmList<Dose> doseList;
    private RealmList<Imunizacao> imunizacaoList;
    private List<Imunizacao> imunizacaoHpvList;
    Context context;

    public CartaoPdfVacinaRVA(RealmList<Vacina> vacinaList, RealmList<Dose> doseList, RealmList<Imunizacao> imunizacaoList, List<Imunizacao> imunizacaoHpvList, Context mContext) {
        this.vacinaList = vacinaList;
        this.doseList = doseList;
        this.imunizacaoList = imunizacaoList;
        this.imunizacaoHpvList = imunizacaoHpvList;
        this.context = mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cartao_pdf_vacinas, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        Vacina itemVacina = vacinaList.get(i);
        Dose itemDose = doseList.get(i);

        holder.textTituloVacina.setText(itemVacina.getVaciNome());
        holder.textTituloDose.setText(itemDose.getDoseDescricao());

        if (!imunizacaoList.isEmpty()) {

            for (Imunizacao itemImunizacao : imunizacaoList) {
                if (!"HPV".equalsIgnoreCase(itemImunizacao.getVacina().getVaciNome())) {
                    if (itemVacina.getId() == itemImunizacao.getVacina().getId()) {
                        holder.textImunizacaoData.setText(formatStringBold("Data:") + " " + getParseDateString(itemImunizacao.getImunData()));
                        holder.textImunizacaoLote.setText(formatStringBold("Lote:") + " " + itemImunizacao.getImunLote());
                        holder.textImunizacaoAplic.setText(formatStringBold("Aplic.:") + " " + itemImunizacao.getImunAgente());
                        holder.textImunizacaoUnid.setText(formatStringBold("Unid.:") + " " + itemImunizacao.getImunPosto());
                    }
                } else if (!imunizacaoHpvList.isEmpty()) {
                    holder.layoutVacinaComum.setVisibility(View.GONE);
                    holder.layoutVacinaHPV1.setVisibility(View.VISIBLE);
                    holder.layoutVacinaHPV2.setVisibility(View.VISIBLE);

                    if (!imunizacaoHpvList.isEmpty()) {
                        Imunizacao imunizacaoHpv1 = imunizacaoHpvList.get(0);
                        holder.textTituloDose.setText("1ª Dose");
                        holder.textImunizacaoDataHPV1.setText("Data: " + getParseDateString(imunizacaoHpv1.getImunData()));
                        holder.textImunizacaoLoteHPV1.setText("Lote: " + imunizacaoHpv1.getImunLote());
                        holder.textImunizacaoAplicHPV1.setText("Aplic.: " + imunizacaoHpv1.getImunAgente());
                        holder.textImunizacaoUnidHPV1.setText("Unid.: " + imunizacaoHpv1.getImunPosto());

                        if (imunizacaoHpvList.size() > 1) {
                            Imunizacao imunizacaoHpv2 = imunizacaoHpvList.get(1);
                            holder.textTituloDoseHPV2.setText("2ª Dose");
                            holder.textImunizacaoDataHPV2.setText("Data: " + getParseDateString(imunizacaoHpv2.getImunData()));
                            holder.textImunizacaoLoteHPV2.setText("Lote: " + imunizacaoHpv2.getImunLote());
                            holder.textImunizacaoAplicHPV2.setText("Aplic.: " + imunizacaoHpv2.getImunAgente());
                            holder.textImunizacaoUnidHPV2.setText("Unid.: " + imunizacaoHpv2.getImunPosto());

                        }
                    }
                }
            }

        }
    }

    private String formatStringBold(String text) {
        String format = "<b>" + text + "</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(format, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(format).toString();
        }
    }

    @Override
    public int getItemCount() {
        return (null != vacinaList ? vacinaList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_vacinas)
        protected LinearLayout layoutVacinas;

        //Layouts Vacina/Doses
        @BindView(R.id.layout_imunizacao_comum)
        protected LinearLayout layoutVacinaComum;

        @BindView(R.id.layout_imunizacao_hpv1)
        protected LinearLayout layoutVacinaHPV1;

        @BindView(R.id.layout_imunizacao_hpv2)
        protected LinearLayout layoutVacinaHPV2;

        @BindView(R.id.text_pdf_vacina)
        protected TextView textTituloVacina;

        @BindView(R.id.text_pdf_dose)
        protected TextView textTituloDose;

        //Comum
        @BindView(R.id.text_pdf_imunizacao_data)
        protected TextView textImunizacaoData;

        @BindView(R.id.text_pdf_imunizacao_lote)
        protected TextView textImunizacaoLote;

        @BindView(R.id.text_pdf_imunizacao_aplicador)
        protected TextView textImunizacaoAplic;

        @BindView(R.id.text_pdf_imunizacao_unidade)
        protected TextView textImunizacaoUnid;


        //HPV 1ª Dose
        @BindView(R.id.text_pdf_imunizacao_data_hpv1)
        protected TextView textImunizacaoDataHPV1;

        @BindView(R.id.text_pdf_imunizacao_lote_hpv1)
        protected TextView textImunizacaoLoteHPV1;

        @BindView(R.id.text_pdf_imunizacao_aplicador_hpv1)
        protected TextView textImunizacaoAplicHPV1;

        @BindView(R.id.text_pdf_imunizacao_unidade_hpv1)
        protected TextView textImunizacaoUnidHPV1;


        //Titulo HPV 2ª Dose
        @BindView(R.id.text_pdf_dose_hpv2)
        protected TextView textTituloDoseHPV2;

        //HPV 2ª Dose
        @BindView(R.id.text_pdf_imunizacao_data_hpv2)
        protected TextView textImunizacaoDataHPV2;

        @BindView(R.id.text_pdf_imunizacao_lote_hpv2)
        protected TextView textImunizacaoLoteHPV2;

        @BindView(R.id.text_pdf_imunizacao_aplicador_hpv2)
        protected TextView textImunizacaoAplicHPV2;

        @BindView(R.id.text_pdf_imunizacao_unidade_hpv2)
        protected TextView textImunizacaoUnidHPV2;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
package br.com.erivando.proimuni.ui.activity.imunizacao;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BasePresenter;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import br.com.erivando.proimuni.util.rx.SchedulerProvider;
import br.com.erivando.proimuni.R;
import io.reactivex.disposables.CompositeDisposable;

import static br.com.erivando.proimuni.util.Uteis.getCapitalizeNome;
import static br.com.erivando.proimuni.util.Uteis.getCurrentTimeStamp;
import static br.com.erivando.proimuni.util.Uteis.getParseDateString;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 16:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class ImunizacaoPresenter<V extends ImunizacaoMvpView> extends BasePresenter<V> implements ImunizacaoMvpPresenter<V> {

    @Inject
    public ImunizacaoPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onCadasrarClick(Long id, String imunData, String imunAgente, String imunPosto, String imunLote, Long idVacina, Long idDose, Long idIdade, final Long idCartao) {

        if (imunAgente == null || imunAgente.isEmpty()) {
            getMvpView().onError(R.string.text_valida_agente);
            return;
        }
        if (imunPosto == null || imunPosto.isEmpty()) {
            getMvpView().onError(R.string.text_valida_posto);
            return;
        }
        if (imunLote == null || imunLote.isEmpty()) {
            getMvpView().onError(R.string.text_valida_lote);
            return;
        }

        getMvpView().showLoading();
        imunAgente = getCapitalizeNome(imunAgente.trim());
        imunPosto = getCapitalizeNome(imunPosto.trim());
        Vacina vacina = getIDataManager().obtemVacina(idVacina);
        Dose dose = getIDataManager().obtemDose(idDose);
        Idade idade = getIDataManager().obtemIdade(idIdade);
        Cartao cartao = getIDataManager().obtemCartao(idCartao);

        Imunizacao imunizacao = new Imunizacao();
        imunizacao.setId((id == 0L) ? (long) getIDataManager().getImunizacaoID().incrementAndGet() : id);
        imunizacao.setImunData((imunData != null) ? getParseDateString(imunData).getTime() : getParseDateString(getCurrentTimeStamp()).getTime());
        imunizacao.setImunAgente(imunAgente);
        imunizacao.setImunPosto(imunPosto);
        imunizacao.setImunLote(imunLote);
        imunizacao.setVacina(vacina);
        imunizacao.setDose(dose);
        imunizacao.setIdade(idade);
        imunizacao.setCartao(cartao);

        try {
            if (onNovoAtualizaImunizacao(imunizacao)) {
                getMvpView().showMessage(R.string.text_cadastro_sucesso);
            } else {
                getMvpView().showMessage(R.string.text_valida_cadastro);
                return;
            }
            if (!isViewAttached()) {
                return;
            }

            new AlertDialog.Builder(getMvpView().getContextActivity())
                    .setIcon(R.drawable.ic_launcher_round)
                    .setTitle(AppAplicacao.contextApp.getResources().getString(R.string.app_name))
                    .setMessage("Parabéns! \nImunização registrada com sucesso!")
                    .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getMvpView().openCartaoDetalheActivity(idCartao);

                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();

        } catch (Exception ex) {
            getMvpView().onError(ex.getMessage());
            getMvpView().onError(R.string.erro_text_cadastro);
        } finally {
            getMvpView().hideLoading();
        }
    }

    @Override
    public boolean onNovoAtualizaImunizacao(Imunizacao imunizacao) {
        return getIDataManager().novoAtualizaImunizacao(imunizacao);
    }

    @Override
    public Imunizacao onImunizacaoCadastrada(String[] strings, Long[] longs) {
        return getIDataManager().obtemImunizacao(strings, longs);
    }

}
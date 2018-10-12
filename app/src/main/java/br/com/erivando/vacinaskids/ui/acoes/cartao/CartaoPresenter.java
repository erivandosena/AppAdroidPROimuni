package br.com.erivando.vacinaskids.ui.acoes.cartao;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static br.com.erivando.vacinaskids.util.Uteis.bitmapParaBase64;
import static br.com.erivando.vacinaskids.util.Uteis.capitalizeNome;
import static br.com.erivando.vacinaskids.util.Uteis.isDataValida;
import static br.com.erivando.vacinaskids.util.Uteis.parseDateString;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoPresenter<V extends CartaoMvpView> extends BasePresenter<V> implements CartaoMvpPresenter<V> {

    @Inject
    public CartaoPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onCadasrarClick(Long id, Long idCrianca) {
        getMvpView().showLoading();
        Crianca crianca = getIDataManager().obtemCrianca(idCrianca);
        if (crianca == null) {
            getMvpView().onError(R.string.text_valida_crianca);
            return;
        }

        Cartao cartao = new Cartao();
        cartao.setId((id == 0L) ? (long)getIDataManager().getCartaoID().incrementAndGet() : id);
        cartao.setCrianca(crianca);

        try {
            if (onNovoAtualizaCartao(cartao)) {
                getMvpView().showMessage(R.string.text_cadastro_sucesso);
            } else {
                getMvpView().showMessage(R.string.text_valida_cadastro);
                return;
            }
            if (!isViewAttached()) {
                return;
            }
            getMvpView().openCartaoListaActivity("edita");
        } catch (Exception ex) {
            getMvpView().onError(ex.getMessage());
            getMvpView().onError(R.string.erro_text_cadastro);
        } finally {
            getMvpView().hideLoading();
        }
    }

    @Override
    public boolean onNovoAtualizaCartao(Cartao cartao) {
        return getIDataManager().novoAtualizaCartao(cartao);
    }

    @Override
    public Cartao onCartaoCadastrado(Long id) {
        return getIDataManager().obtemCartao(id);
    }

    @Override
    public Cartao onCartaoCadastradoPorCrianca(Long id) {
        return getIDataManager().obtemCartaoPorCrianca(id);
    }

    @Override
    public List<Cartao> onCartaoCadastradosPorCrianca(Crianca crianca) {
        return getIDataManager().obtemTodosCartoesPorCrianca(crianca);
    }

    @Override
    public List<Cartao> onCartaoCadastrados() {
        return getIDataManager().obtemTodosCartoes();
    }
}
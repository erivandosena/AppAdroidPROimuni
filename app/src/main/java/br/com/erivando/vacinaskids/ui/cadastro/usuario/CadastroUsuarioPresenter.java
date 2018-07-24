package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import android.util.Log;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   21 de Julho de 2018 as 16:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


public class CadastroUsuarioPresenter<V extends CadastroUsuarioMvpView> extends BasePresenter<V> implements CadastroUsuarioMvpPresenter<V> {

    @Inject
    public CadastroUsuarioPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onCadasrarClick(String nome, String login, String senha, String email) {
        getMvpView().showLoading();

        Usuario usuario = new Usuario();
        usuario.setId(getIDataManager().getUsuarioID().incrementAndGet());
        usuario.setUsuaNome(nome);
        usuario.setUsuaLogin(login);
        usuario.setUsuaSenha(senha);
        usuario.setUsuaEmail(email);

        try {
            if (getIDataManager().novoUsuario(usuario)) {
                getMvpView().showMessage(R.string.text_cadastro_sucesso);
            } else {
                getMvpView().showMessage(R.string.text_cadastro_erro);
                return;
            }
            if (!isViewAttached()) {
                return;
            }
            getMvpView().openLoginActivity();
        } catch (Exception ex) {
            getMvpView().onError(ex.getMessage());
            getMvpView().onError(R.string.text_valida_cadastro);
        } finally {
            getMvpView().hideLoading();
        }


        /*
        getMvpView().showLoading();
        try {
            List lista = new ArrayList();
            getIDataManager().updateUserInfo(
                    nome,
                    login,
                    senha,
                    email,
                    true);
            if (!isViewAttached()) {
                return;
            }
            getMvpView().openLoginActivity();
        } catch (Exception ex) {
            getMvpView().onError(ex.getMessage());
            getMvpView().onError(R.string.text_valida_cadastro);
        } finally {
            getMvpView().hideLoading();
        }
        */
    }

    @Override
    public void onLoginClick() {
        getMvpView().openLoginActivity();
    }
}

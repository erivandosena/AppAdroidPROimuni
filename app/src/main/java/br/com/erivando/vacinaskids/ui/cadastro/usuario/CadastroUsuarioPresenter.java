package br.com.erivando.vacinaskids.ui.cadastro.usuario;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.mvp.base.BasePresenter;
import br.com.erivando.vacinaskids.util.CommonUtils;
import br.com.erivando.vacinaskids.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static br.com.erivando.vacinaskids.util.Uteis.capitalizeNome;
import static br.com.erivando.vacinaskids.util.Uteis.validaFrases;

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
    public void onCadasrarClick(String nome, String login, String email, String senha, String repeteSenha) {
        //validando cadastro do usuário
        if ((nome == null) || (nome.isEmpty()) || !nome.matches("^[a-zA-Za-zà-ú]+ [a-zA-ZA-ZÀ-Ú]+.*")) {
            getMvpView().onError(R.string.erro_text_nome_completo);
            return;
        }
        if (login == null || login.isEmpty() || login.length() < 4 || login.length() > 20 || login.matches("^[a-zA-Z]+ [a-zA-Z]+.*")) {
            getMvpView().onError(R.string.erro_text_usuario);
            return;
        }
        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.erro_text_email_invalido);
            return;
        }
        if (senha == null || senha.isEmpty() || senha.length() > 20) {
            getMvpView().onError(R.string.erro_text_senha);
            return;
        }
        if (repeteSenha == null || repeteSenha.isEmpty()) {
            getMvpView().onError(R.string.erro_text_repeteSenha);
            return;
        }
        if (!senha.equals(repeteSenha)) {
            getMvpView().onError(R.string.erro_text_senhaInvalida);
            return;
        }

        nome = capitalizeNome(nome.trim());
        login = login.trim().toLowerCase();
        email = email.trim().toLowerCase();

        getMvpView().showLoading();

        Usuario usuario = new Usuario();
        usuario.setId((long) getIDataManager().getUsuarioID().incrementAndGet());
        usuario.setUsuaNome(nome);
        usuario.setUsuaLogin(login);
        usuario.setUsuaSenha(senha);
        usuario.setUsuaEmail(email);

        try {
            if (getIDataManager().novoUsuario(usuario)) {
                getMvpView().showMessage(R.string.text_cadastro_sucesso);
            } else {
                getMvpView().showMessage(R.string.erro_text_cadastro);
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
    }

    @Override
    public void onLoginClick() {
        getMvpView().openLoginActivity();
    }
}

package br.com.erivando.proimuni.ui.activity.main;

import javax.inject.Inject;

import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 18:01
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    @Inject
    public MainPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public void onDrawerOptionAboutClick() {
        getMvpView().closeNavigationDrawer();
    }

    @Override
    public void onDrawerOptionLogoutClick() {
        getMvpView().showLoading();

        getIDataManager().setUserAsLoggedOut();

        getMvpView().onFacebookSignOut();

        getMvpView().onGooleSignOut();

        getMvpView().hideLoading();

        getMvpView().openLoginActivity();
    }

    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached()) {
            return;
        }
        getMvpView().updateAppVersion();

        final String currentUserName = getIDataManager().getCurrentUserName();
        if (currentUserName != null && !currentUserName.isEmpty()) {
            getMvpView().updateUserName(currentUserName);
        }

        final String currentUserEmail = getIDataManager().getCurrentUserEmail();
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            getMvpView().updateUserEmail(currentUserEmail);
        }

        final String profilePicUrl = getIDataManager().getCurrentUserProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            getMvpView().updateUserProfilePic(profilePicUrl);
        }
    }

    @Override
    public boolean onConfigNotificacoes() {
        return getIDataManager().isNotificacoes();
    }

    @Override
    public void onDrawerRateUsClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showRateUsDialog();
    }

}

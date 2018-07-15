package br.com.erivando.vacinaskids.mvp.base;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 16:28
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.database.DataManager;
import br.com.erivando.vacinaskids.mvp.MvpPresenter;
import br.com.erivando.vacinaskids.mvp.MvpView;
import io.reactivex.disposables.CompositeDisposable;

/**
 Classe base que implementa a interface do Presenter e fornece uma implementação básica para
 onAttach() e onDetach(). Ele também lida com uma referência ao mvpView que pode ser acessado
 a partir das classes filhas chamando getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;
   // private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         /*
                         SchedulerProvider schedulerProvider,
                         */
                         CompositeDisposable compositeDisposable
                         )
    {
        this.mDataManager = dataManager;
        /*
        this.mSchedulerProvider = schedulerProvider;
        */
        this.mCompositeDisposable = compositeDisposable;

    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        //mCompositeDisposable.dispose();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    /*
    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
    */

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void handleApiError(ANError error) {

        /*
        if (error == null || error.getErrorBody() == null) {
            getMvpView().onError(R.string.api_default_error);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.CONNECTION_ERROR)) {
            getMvpView().onError(R.string.connection_error);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.REQUEST_CANCELLED_ERROR)) {
            getMvpView().onError(R.string.api_retry_error);
            return;
        }

        final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

        try {
            ApiError apiError = gson.fromJson(error.getErrorBody(), ApiError.class);

            if (apiError == null || apiError.getMessage() == null) {
                getMvpView().onError(R.string.api_default_error);
                return;
            }

            switch (error.getErrorCode()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    setUserAsLoggedOut();
                    getMvpView().openActivityOnTokenExpire();
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                case HttpsURLConnection.HTTP_NOT_FOUND:
                default:
                    getMvpView().onError(apiError.getMessage());
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            Log.e(TAG, "handleApiError", e);
            getMvpView().onError(R.string.api_default_error);
        }
        */
    }

    @Override
    public void setUserAsLoggedOut() {
       // getDataManager().setAccessToken(null);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Por favor, ligue ao Presenter.onAttach (MvpView) antes de" +
                    "solicitar dados ao apresentador");
        }
    }
}

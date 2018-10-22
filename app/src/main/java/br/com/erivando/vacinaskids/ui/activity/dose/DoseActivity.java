package br.com.erivando.vacinaskids.ui.activity.dose;

import android.content.Context;

import br.com.erivando.vacinaskids.mvp.base.BaseActivity;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 11:01
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class DoseActivity extends BaseActivity implements DoseMvpView {
    @Override
    protected void setUp() {

    }

    @Override
    public Context getContextActivity() {
        return DoseActivity.this;
    }
}

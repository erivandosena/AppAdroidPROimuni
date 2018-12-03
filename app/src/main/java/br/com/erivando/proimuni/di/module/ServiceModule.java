package br.com.erivando.proimuni.di.module;

import android.app.Service;

import dagger.Module;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 22:27
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
@Module
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }
}

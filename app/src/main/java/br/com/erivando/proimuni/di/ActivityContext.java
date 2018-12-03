package br.com.erivando.proimuni.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 13:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContext {

}

package br.com.erivando.vacinaskids.database.api;


import br.com.erivando.vacinaskids.BuildConfig;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   23 de Julho de 2018 as 00:03
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

final class ApiEndPoint {

    public static final String ENDPOINT_GOOGLE_LOGIN = BuildConfig.BASE_URL
            + "/588d14f4100000a9072d2943";

    public static final String ENDPOINT_FACEBOOK_LOGIN = BuildConfig.BASE_URL
            + "/588d15d3100000ae072d2944";

    public static final String ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL
            + "/588d15f5100000a8072d2945";

    public static final String ENDPOINT_LOGOUT = BuildConfig.BASE_URL
            + "/588d161c100000a9072d2946";

    public static final String ENDPOINT_BLOG = BuildConfig.BASE_URL
            + "/5926ce9d11000096006ccb30";

    public static final String ENDPOINT_OPEN_SOURCE = BuildConfig.BASE_URL
            + "/5926c34212000035026871cd";

    private ApiEndPoint() {
        // Esta classe não é publicably instanciável
    }

}
package br.com.erivando.vacinaskids.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class Uteis {

    /**
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * Habilita modo tela cheia
     *
     * @param context Context o contexto da activity
     */
    public static void habilitaImmersiveMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     *
     * @param context
     */
    public static void habilitaTelaCheia(Context context) {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     *
     * @param nomeCompleto
     * @return
     */
    public static String capitalizeNome(final String nomeCompleto) {
        String[] palavras = nomeCompleto.split(" ");
        StringBuilder sb = new StringBuilder();
        List<String> excessoes = new ArrayList<String>(Arrays.asList("de", "da", "das", "do", "dos", "na", "nas", "no", "nos", "a", "e", "o", "em", "com"));
        for (String palavra : palavras) {
            if (excessoes.contains(palavra.toLowerCase()))
                sb.append(palavra.toLowerCase()).append(" ");
            else
                sb.append(Character.toUpperCase(palavra.charAt(0))).append(palavra.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     *
     * @param nomes
     * @return
     */
    public static boolean validaFrases(String nomes){
        return (nomes != null) && (!nomes.isEmpty()) && (nomes.split(" ").length >= 2);
    }
}

package br.com.erivando.vacinaskids.util;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 20:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class TransactionTime {
    private long start;
    private long end;

    public TransactionTime(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDuration() {
        if (this.start > 0 && this.end > 0) {
            return this.end - this.start;
        } else {
            return 0;
        }
    }
}

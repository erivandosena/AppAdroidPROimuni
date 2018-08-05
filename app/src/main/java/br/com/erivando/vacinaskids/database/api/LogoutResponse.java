package br.com.erivando.vacinaskids.database.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Julho de 2018 as 23:05
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class LogoutResponse {
    @Expose
    @SerializedName("status_code")
    private String statusCode;

    @Expose
    @SerializedName("message")
    private String message;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        LogoutResponse that = (LogoutResponse) object;

        return (statusCode != null ? statusCode.equals(that.statusCode) : that.statusCode == null) && (message != null ? message.equals(that.message) : that.message == null);

    }

    @Override
    public int hashCode() {
        int result = statusCode != null ? statusCode.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
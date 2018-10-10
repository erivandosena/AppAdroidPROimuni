package br.com.erivando.vacinaskids.database.importacao;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.RealmDataBase;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.ui.AppAplicacao;
import br.com.erivando.vacinaskids.util.TransactionTime;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 20:30
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class RealmImport {

    private static Realm realm;

    public static void importFromJson(final Resources resources) {
        realm = new RealmDataBase(AppAplicacao.contextApp).getRealmInstance();

        //timer
        final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                InputStream inputStreamIdade = resources.openRawResource(R.raw.idade);
                InputStream inputStreamDose = resources.openRawResource(R.raw.dose);
                InputStream inputStreamVacina = resources.openRawResource(R.raw.vacina);
                InputStream inputStreamCalendario = resources.openRawResource(R.raw.calendario);
                try {
                    realm.createOrUpdateAllFromJson(Idade.class, inputStreamIdade);
                    realm.createOrUpdateAllFromJson(Dose.class, inputStreamDose);
                    realm.createOrUpdateAllFromJson(Vacina.class, inputStreamVacina);

                   // Log.d("Idade; ", realm.where(Idade.class).findAll().toString());
                   // Log.d("Dose; ", realm.where(Dose.class).findAll().toString());
                   // Log.d("Vacina; ", realm.where(Vacina.class).findAll().toString());

                    JsonElement element = new JsonParser().parse(new InputStreamReader(inputStreamCalendario));
                    JSONArray jsonArray = new JSONArray(element.getAsJsonArray().toString());
                    JSONObject jsonObject;
                    for(int i=0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Calendario calendario = new Calendario();
                        calendario.setId(jsonObject.getLong("id"));
                        calendario.setIdade(realm.where(Idade.class).equalTo("id", jsonObject.getLong("idade")).findFirst());
                        calendario.setVacina(realm.where(Vacina.class).equalTo("id",jsonObject.getLong("vacina")).findFirst());
                        calendario.setDose(realm.where(Dose.class).equalTo("id", jsonObject.getLong("dose")).findFirst());
                        realm.insertOrUpdate(calendario);
                    }

                    transactionTime.setEnd(System.currentTimeMillis());

                   // Log.d("Calendario; ", realm.where(Calendario.class).sort("id", Sort.ASCENDING).findAll().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }

            }
        });
        //Log.d("Realm ", "Tarefa concluída em " + transactionTime.getDuration() + "ms");
    }
}

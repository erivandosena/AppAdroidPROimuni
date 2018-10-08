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
import br.com.erivando.vacinaskids.database.model.Controle;
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
        //Realm realm = Realm.getDefaultInstance();
        realm = new RealmDataBase(AppAplicacao.contextApp).getRealmInstance();

        //transaction timer
        final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                InputStream inputStreamVacina = resources.openRawResource(R.raw.vacina);
                InputStream inputStreamIdade = resources.openRawResource(R.raw.idade);
                InputStream inputStreamControle = resources.openRawResource(R.raw.controle);
                try {
                    realm.createOrUpdateAllFromJson(Vacina.class, inputStreamVacina);
                    realm.createOrUpdateAllFromJson(Idade.class, inputStreamIdade);


                    JsonElement element = new JsonParser().parse(new InputStreamReader(inputStreamControle));
                    JSONArray jsonArray = new JSONArray(element.getAsJsonArray().toString());
                    JSONObject jsonObject = new JSONObject();

                    for(int i=0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Controle controle = new Controle();
                        controle.setId(jsonObject.getLong("id"));
                        controle.setIdade(new Idade(jsonObject.getJSONArray("idade").getJSONObject(0).getLong("id"), jsonObject.getJSONArray("idade").getJSONObject(0).getString("idadDescricao")));
                        controle.setVacina(new Vacina(jsonObject.getJSONArray("vacina").getJSONObject(0).getLong("id"), jsonObject.getJSONArray("vacina").getJSONObject(0).getString("vaciNome"), jsonObject.getJSONArray("vacina").getJSONObject(0).getString("vaciDescricao"), jsonObject.getJSONArray("vacina").getJSONObject(0).getString("vaciAdministracao")));
                        ///controle.setDose(new Dose(1L, null, null, null, null));
                        realm.insertOrUpdate(controle);
                    }

                    transactionTime.setEnd(System.currentTimeMillis());

                    Log.d("Vacina; ", realm.where(Vacina.class).findAll().toString());
                    Log.d("Idade; ", realm.where(Idade.class).findAll().toString());
                    Log.d("Controle; ", realm.where(Controle.class).sort("id", Sort.ASCENDING).findAll().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }
            }
        });
        Log.d("Realm ", "Tarefa concluída em " + transactionTime.getDuration() + "ms");
    }
}

package br.com.erivando.vacinaskids.database;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.di.ApplicationContext;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Julho de 2018 as 00:36
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class RealmDataBase implements IRealm {

    private static RealmConfiguration realmConfiguration;
    private final Context context;

    @Inject
    public RealmDataBase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void setup(@ApplicationContext Context context) {
        Realm.init(context);
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration.Builder().schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        }
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    @Override
    public <T extends RealmObject> boolean getLoginLocal(Class<T> clazz, String login, String senha) {
        Realm realm = getRealmInstance();
        try {
            return realm.where(clazz).equalTo("usuaLogin", login).and().equalTo("usuaSenha", senha).count() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            realm.close();
        }
    }

    @Override
    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    @Override
    public <T extends RealmObject> boolean addOrUpdate(T model) {
        Realm realm = getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(model);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    @Override
    public <T extends RealmObject> boolean remove(Class<T> clazz, String campo, Long valor) {
        Realm realm = getRealmInstance();
        try {
            realm.beginTransaction();
            realm.where(clazz).equalTo(campo, valor).findFirst().deleteFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).findFirst();
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz, String campo, Long valor) {
        Realm realm = getRealmInstance();
        T realmObject = realm.where(clazz).equalTo(campo, valor).findFirst();
        if (realmObject != null)
            return realm.copyFromRealm(realmObject);
        else
            return realmObject;
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz, String campo, String valor) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).findFirst();
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz, String[] valores) {
        return getRealmInstance().where(clazz).equalTo(valores[0], valores[1]).equalTo(valores[2], valores[3]).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(String campo, Long valor, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(String campo, String valor, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).findAll();
    }

    @Override
    public <T extends RealmObject> AtomicInteger getIdByClassModel(Class<T> clazz) {
        Realm realm = getRealmInstance();
        RealmResults<T> results = realm.where(clazz).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }

    @Override
    public void getImportJson() {
        final Resources resources = context.getResources();

        InputStream inputStreamIdade = resources.openRawResource(R.raw.idade);
        InputStream inputStreamDose = resources.openRawResource(R.raw.dose);
        InputStream inputStreamVacina = resources.openRawResource(R.raw.vacina);
        InputStream inputStreamCalendario = resources.openRawResource(R.raw.calendario);

        Realm realm = getRealmInstance();
        try {
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Idade.class, inputStreamIdade);
            realm.createOrUpdateAllFromJson(Dose.class, inputStreamDose);
            realm.createOrUpdateAllFromJson(Vacina.class, inputStreamVacina);

            JsonElement element = new JsonParser().parse(new InputStreamReader(inputStreamCalendario));
            JSONArray jsonArray = new JSONArray(element.getAsJsonArray().toString());
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Calendario calendario = new Calendario();
                calendario.setId(jsonObject.getLong("id"));
                calendario.setIdade(realm.where(Idade.class).equalTo("id", jsonObject.getLong("idade")).findFirst());
                calendario.setVacina(realm.where(Vacina.class).equalTo("id", jsonObject.getLong("vacina")).findFirst());
                calendario.setDose(realm.where(Dose.class).equalTo("id", jsonObject.getLong("dose")).findFirst());
                realm.insertOrUpdate(calendario);
            }
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }
}

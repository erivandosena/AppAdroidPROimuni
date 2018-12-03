package br.com.erivando.proimuni.database;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.di.ApplicationContext;
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
        Realm realm = getRealmInstance();
        T realmObject = null;
        if(valores.length  == 2)
            realmObject = realm.where(clazz).equalTo(valores[0], valores[1]).findFirst();
        if(valores.length  == 4)
            realmObject = realm.where(clazz).equalTo(valores[0], valores[1]).equalTo(valores[2], valores[3]).findFirst();
        if(valores.length  == 6)
            realmObject = realm.where(clazz).equalTo(valores[0], valores[1]).equalTo(valores[2], valores[3]).equalTo(valores[4], valores[5]).findFirst();

        return realmObject;
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz, String[] strings, Long[] longs) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(strings[0], longs[0]).equalTo(strings[1], longs[1]).equalTo(strings[2], longs[2]).findFirst();
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

    public <T extends RealmObject> List<T> findAll(String[] campo, Long[] valor, Class<T> clazz) {
        Realm realm = getRealmInstance();
        List<T> lista = new ArrayList<T>();
        if(campo.length  == 1 && valor.length == 1)
            lista = realm.where(clazz).equalTo(campo[0], valor[0]).findAll();
        if(campo.length  == 2 && valor.length == 2)
            lista = realm.where(clazz).equalTo(campo[0], valor[0]).equalTo(campo[1], valor[1]).findAll();
        if(campo.length  == 3 && valor.length == 3)
            lista = realm.where(clazz).equalTo(campo[0], valor[0]).equalTo(campo[1], valor[1]).equalTo(campo[2], valor[2]).findAll();
        if(campo.length  == 4 && valor.length == 4)
            lista = realm.where(clazz).equalTo(campo[0], valor[0]).equalTo(campo[1], valor[1]).equalTo(campo[2], valor[2]).equalTo(campo[3], valor[3]).findAll();
        if(campo.length  == 5 && valor.length == 5)
            lista = realm.where(clazz).equalTo(campo[0], valor[0]).equalTo(campo[1], valor[1]).equalTo(campo[2], valor[2]).equalTo(campo[3], valor[3]).equalTo(campo[4], valor[4]).findAll();
        return lista;
    }

    @Override
    public <T extends RealmObject> List<T> findNotAll(String[] campo, Long[] valor, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).notEqualTo(campo[0], valor[0]).notEqualTo(campo[1], valor[1]).notEqualTo(campo[2], valor[2]).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(String campo, String valor, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAllOrderBy(String orderBy, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).sort(orderBy).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAllOrderBy(String campo, Long valor, String orderBy, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).sort(orderBy).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAllOrderBy(String campo, String valor, String orderBy, Class<T> clazz) {
        Realm realm = getRealmInstance();
        return realm.where(clazz).equalTo(campo, valor).sort(orderBy).findAll();
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

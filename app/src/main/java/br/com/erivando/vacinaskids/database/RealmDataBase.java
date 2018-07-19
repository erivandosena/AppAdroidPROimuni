package br.com.erivando.vacinaskids.database;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.erivando.vacinaskids.di.ApplicationContext;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Julho de 2018 as 00:36
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

@Singleton
public class RealmDataBase implements IRealm {

    private final Context context;

    @Inject
    public RealmDataBase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void setup(@ApplicationContext Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        getRealmInstance().close();
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
    public <T extends RealmObject> boolean remove(Class<T> clazz, String field, Long id) {
        Realm realm = getRealmInstance();
        try {
            realm.beginTransaction();
            realm.where(clazz).equalTo(field, id).findFirst().deleteFromRealm();
            realm.commitTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    @Override
    public <T extends RealmObject> boolean addOrUpdate(T model) {
        Realm realm = getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(model);
            realm.commitTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz) {
        return getRealmInstance().where(clazz).findFirst();
    }

    @Override
    public <T extends RealmObject> T getObject(Class<T> clazz, String field, Long value) {
        return getRealmInstance().where(clazz).equalTo(field, value).findFirst();
    }

    @Override
    public <T extends RealmObject> T getObject(String[] fieldsObject, String[] valuesObject, Class<T> clazz) {
        return getRealmInstance().where(clazz).equalTo(fieldsObject[0], valuesObject[0]).equalTo(fieldsObject[1], valuesObject[1]).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> findAll(String[] fieldsObject, String[] valuesObject, Class<T> clazz) {
        return getRealmInstance().where(clazz).equalTo(fieldsObject[0], valuesObject[0]).equalTo(fieldsObject[1], valuesObject[1]).findAll();
    }

}

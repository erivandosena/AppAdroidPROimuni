package br.com.erivando.vacinaskids.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Julho de 2018 as 06:38
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

interface IRealm {

    void setup(Context context);

    Realm getRealmInstance();

    <T extends RealmObject> T add(T model);

    <T extends RealmObject> boolean remove(Class<T> clazz, String field, Long value);

    <T extends RealmObject> boolean addOrUpdate(T model);

    <T extends RealmObject> T getObject(Class<T> clazz);

    <T extends RealmObject> T getObject(Class<T> clazz, String field, Long value);

    <T extends RealmObject> RealmObject getObject(Class<T> clazz, String[] fieldValues);

    <T extends RealmObject> T getObject(Class<T> clazz, String[] fieldValueA, String[] fieldValueB);

    <T extends RealmObject> List<T> findAll(Class<T> clazz);

    <T extends RealmObject> List<T> findAll(String[] fieldsObject, String[] valuesObject, Class<T> clazz);

    <T extends RealmObject> AtomicInteger getIdByClassModel( Class<T> clazz);

    void close();

    <T extends RealmObject> boolean getLoginLocal(Class<T> clazz, String login, String senha);
}

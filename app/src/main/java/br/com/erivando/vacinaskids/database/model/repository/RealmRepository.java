package br.com.erivando.vacinaskids.database.model.repository;

import java.io.Serializable;
import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   23 de Julho de 2018 as 13:50
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public interface RealmRepository<T extends RealmObject, ID extends Serializable> {

    T findOne(Realm realm, ID id);

    RealmResults<T> findAll(Realm realm);

    void insertOrUpdate(Realm realm, T t);

    void insertOrUpdate(Realm realm, Collection<T> t);

    T saveOrUpdate(Realm realm, T t);

    RealmList<T> saveOrUpdate(Realm realm, RealmList<T> tList);

    RealmQuery<T> query(Realm realm);

    void delete(Realm realm, ID id);

    void delete(Realm realm, T t);

    void deleteAll(Realm realm, RealmResults<T> realmResults);

    void deleteEveryObject(Realm realm);

    long count(Realm realm);
}
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

public abstract class BaseRealmRepositoryImpl<T extends RealmObject, ID extends Serializable> implements RealmRepository<T, ID> {

    private final Class<T> clazz;

    public BaseRealmRepositoryImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RealmResults<T> findAll(Realm realm) {
        return query(realm).findAll();
    }

    @Override
    public void insertOrUpdate(Realm realm, T t) {
        realm.insertOrUpdate(t);
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<T> collection) {
        realm.insertOrUpdate(collection);
    }

    @Override
    public T saveOrUpdate(Realm realm, T t) {
        return realm.copyToRealmOrUpdate(t);
    }

    @Override
    public RealmList<T> saveOrUpdate(Realm realm, RealmList<T> list) {
        RealmList<T> realmList = new RealmList<>();
        for(T t : realm.copyToRealmOrUpdate(list)) {
            realmList.add(t);
        }
        return realmList;
    }

    @Override
    public RealmQuery<T> query(Realm realm) {
        return realm.where(clazz);
    }

    @Override
    public void deleteEveryObject(Realm realm) {
        realm.delete(clazz);
    }

    @Override
    public void delete(Realm realm, T t) {
        t.deleteFromRealm();
    }

    @Override
    public void deleteAll(Realm realm, RealmResults<T> realmResults) {
        realmResults.deleteAllFromRealm();
    }

    @Override
    public long count(Realm realm) {
        return query(realm).count();

    }
}
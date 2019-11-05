package com.mmorpg.framework.asyncdb;

public interface Synchronizer<T> {

    boolean insertData(T object);

    boolean updateData(T object);

    boolean deleteData(T object);
}

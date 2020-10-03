package com.blogspot.e_kanivets.moneytracker.repo.base;

import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Interface that represents a contract of access to abstract storage with CRUD operations.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public interface IRepo<T> {
    /**
     * Creates a record with given fields values in storage.
     *
     * @param instance is entity instance to be created.
     * @return created record or null if can't create.
     */
    @Nullable
    T create(@Nullable T instance);

    /**
     * Reads a record from storage.
     *
     * @param id is identification number of record to be read.
     * @return read record or null if one not exist.
     */
    @Nullable
    T read(long id);

    /**
     * Updates a record in storage.
     *
     * @param instance is entity instance to be updated.
     * @return updated record or null if can't update.
     */
    @Nullable
    T update(@Nullable T instance);

    /**
     * Deletes a record from storage.
     *
     * @param instance is entity instance to be deleted.
     * @return true if deleted or false if not.
     */
    boolean delete(@Nullable T instance);

    /**
     * Reads all records from storage.
     *
     * @return list of all records. List can't be null, but may be zero sized.
     */
    @NonNull
    List<T> readAll();

    /**
     * Reads all records from storage that matches given condition. The same as in standard Android
     * {@link SQLiteOpenHelper}.
     *
     * @param condition is a string of selection. For example - "time BETWEEN ? AND ?".
     * @param args      is a string array with data to be inserted in condition string instead of '?'.
     * @return list of matched records. List can't be null, but may be zero sized.
     */
    @NonNull
    List<T> readWithCondition(@Nullable String condition, @Nullable String[] args);
}

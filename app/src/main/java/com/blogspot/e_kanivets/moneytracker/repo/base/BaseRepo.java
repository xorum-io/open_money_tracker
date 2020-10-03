package com.blogspot.e_kanivets.moneytracker.repo.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.entity.base.IEntity;

import java.util.List;

import timber.log.Timber;

/**
 * Base implementation of {@link IRepo}.
 * No need to call db.close() at all, because SQLiteOpenHelper manage it for us + cache instances.
 * It will speed up all DB operations.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseRepo<T extends IEntity> implements IRepo<T> {
    protected DbHelper dbHelper;

    public BaseRepo(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @NonNull
    abstract protected String getTable();

    @Nullable
    abstract protected ContentValues contentValues(@Nullable T instance);

    @NonNull
    abstract protected List<T> getListFromCursor(@Nullable Cursor cursor);

    @Nullable
    @Override
    public T create(@Nullable T instance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(getTable(), null, contentValues(instance));

        if (id == -1) {
            Timber.d("Couldn't create record: %s", instance);
            return null;
        } else {
            T createdInstance = read(id);
            Timber.d("Created record: %s", createdInstance);
            return createdInstance;
        }
    }

    @Nullable
    @Override
    public T read(long id) {
        List<T> list = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (list.size() == 1) return list.get(0);
        else return null;
    }

    @Nullable
    @Override
    public T update(@Nullable T instance) {
        if (instance == null) return null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Long.valueOf(instance.getId()).toString()};
        long rowsAffected = db.update(getTable(), contentValues(instance), "id=?", args);

        if (rowsAffected == 0) {
            Timber.d("Couldn't update record: %s", instance);
            return null;
        } else {
            T updatedInstance = read(instance.getId());
            Timber.d("Updated record: %s", updatedInstance);
            return updatedInstance;
        }
    }

    @NonNull
    @Override
    public List<T> readAll() {
        return readWithCondition(null, null);
    }

    @Override
    public boolean delete(@Nullable T instance) {
        if (instance == null) return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Long.toString(instance.getId())};
        long rowsAffected = db.delete(getTable(), "id=?", args);

        Timber.d("%s %s deleted", instance, (rowsAffected == 0 ? " didn't " : " "));

        return rowsAffected != 0;
    }

    @NonNull
    @Override
    public List<T> readWithCondition(@Nullable String condition, @Nullable String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(getTable(), null, condition, args, null, null, null);
        List<T> recordList = getListFromCursor(cursor);

        cursor.close();

        return recordList;
    }
}

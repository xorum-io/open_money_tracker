package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Category} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryRepo extends BaseRepo<Category> {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryRepo";

    public CategoryRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @NonNull
    @Override
    protected String getTable() {
        return DbHelper.TABLE_CATEGORIES;
    }

    @Nullable
    @Override
    protected ContentValues contentValues(@Nullable Category category) {
        ContentValues contentValues = new ContentValues();
        if (category == null) return null;

        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        return contentValues;
    }

    @NonNull
    @Override
    protected List<Category> getListFromCursor(@Nullable Cursor cursor) {
        List<Category> categoryList = new ArrayList<>();
        if (cursor == null) return categoryList;

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int nameColIndex = cursor.getColumnIndex(DbHelper.NAME_COLUMN);

            do {
                //Read a record from DB
                Category category = new Category(cursor.getLong(idColIndex),
                        cursor.getString(nameColIndex));

                //Add record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        return categoryList;
    }
}

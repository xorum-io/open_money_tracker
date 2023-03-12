package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Record} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordRepo extends BaseRepo<Record> {
    @SuppressWarnings("unused")
    private static final String TAG = "RecordRepo";

    public RecordRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @NonNull
    @Override
    protected String getTable() {
        return DbHelper.TABLE_RECORDS;
    }

    @Nullable
    @Override
    protected ContentValues contentValues(@Nullable Record record) {
        ContentValues contentValues = new ContentValues();
        if (record == null || record.getCategory() == null || record.getAccount() == null)
            return null;

        contentValues.put(DbHelper.TIME_COLUMN, record.getTime());
        contentValues.put(DbHelper.TYPE_COLUMN, record.getType());
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategory().getId());
        contentValues.put(DbHelper.NOTES_COLUMN, record.getNotes());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccount().getId());
        contentValues.put(DbHelper.CURRENCY_COLUMN, record.getCurrency());
        contentValues.put(DbHelper.DECIMALS_COLUMN, record.getDecimals());

        return contentValues;
    }

    @NonNull
    @Override
    protected List<Record> getListFromCursor(Cursor cursor) {
        List<Record> recordList = new ArrayList<>();
        if (cursor == null) return recordList;

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN);
            int notesColIndex = cursor.getColumnIndex(DbHelper.NOTES_COLUMN);
            int priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN);
            int accountIdColIndex = cursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN);
            int currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN);
            int decimalsColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_COLUMN);

            do {
                Record record = new Record(cursor.getLong(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getLong(categoryColIndex),
                        cursor.getString(notesColIndex),
                        cursor.getLong(priceColIndex),
                        cursor.getLong(accountIdColIndex),
                        cursor.getString(currencyColIndex),
                        cursor.getLong(decimalsColIndex));

                recordList.add(record);
            } while (cursor.moveToNext());
        }

        return recordList;
    }
}

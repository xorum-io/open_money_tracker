package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link ExchangeRate} entity.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateRepo extends BaseRepo<ExchangeRate> {
    @SuppressWarnings("unused")
    private static final String TAG = "ExchangeRateRepo";

    public ExchangeRateRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @NonNull
    @Override
    protected String getTable() {
        return DbHelper.TABLE_RATES;
    }

    @Nullable
    @Override
    protected ContentValues contentValues(@Nullable ExchangeRate exchangeRate) {
        ContentValues contentValues = new ContentValues();
        if (exchangeRate == null) return null;

        contentValues.put(DbHelper.CREATED_AT_COLUMN, exchangeRate.getCreatedAt());
        contentValues.put(DbHelper.FROM_CURRENCY_COLUMN, exchangeRate.getFromCurrency());
        contentValues.put(DbHelper.TO_CURRENCY_COLUMN, exchangeRate.getToCurrency());
        contentValues.put(DbHelper.AMOUNT_COLUMN, exchangeRate.getAmount());

        return contentValues;
    }

    @NonNull
    @Override
    protected List<ExchangeRate> getListFromCursor(@Nullable Cursor cursor) {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        if (cursor == null) return exchangeRateList;

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int createdAtColIndex = cursor.getColumnIndex(DbHelper.CREATED_AT_COLUMN);
            int fromCurrencyColIndex = cursor.getColumnIndex(DbHelper.FROM_CURRENCY_COLUMN);
            int toCurrencyColIndex = cursor.getColumnIndex(DbHelper.TO_CURRENCY_COLUMN);
            int amountColIndex = cursor.getColumnIndex(DbHelper.AMOUNT_COLUMN);

            do {
                //Read a record from DB
                ExchangeRate category = new ExchangeRate(cursor.getLong(idColIndex),
                        cursor.getLong(createdAtColIndex),
                        cursor.getString(fromCurrencyColIndex),
                        cursor.getString(toCurrencyColIndex),
                        cursor.getDouble(amountColIndex));

                //Add record to list
                exchangeRateList.add(category);
            } while (cursor.moveToNext());
        }

        return exchangeRateList;
    }
}

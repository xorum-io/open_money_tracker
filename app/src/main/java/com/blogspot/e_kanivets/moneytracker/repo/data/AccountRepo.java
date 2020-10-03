package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Account} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountRepo extends BaseRepo<Account> {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountRepo";

    public AccountRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @NonNull
    @Override
    protected String getTable() {
        return DbHelper.TABLE_ACCOUNTS;
    }

    @Nullable
    @Override
    protected ContentValues contentValues(@Nullable Account account) {
        ContentValues contentValues = new ContentValues();
        if (account == null) return null;

        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());
        contentValues.put(DbHelper.DECIMALS_COLUMN, account.getDecimals());
        contentValues.put(DbHelper.GOAL_COLUMN, account.getGoal());
        contentValues.put(DbHelper.ARCHIVED_COLUMN, account.isArchived());
        contentValues.put(DbHelper.COLOR_COLUMN, account.getColor());

        return contentValues;
    }

    @NonNull
    @Override
    protected List<Account> getListFromCursor(@Nullable Cursor cursor) {
        List<Account> accountList = new ArrayList<>();
        if (cursor == null) return accountList;

        if (cursor.moveToFirst()) {
            // Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int curSumColIndex = cursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN);
            int currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN);
            int decimalsColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_COLUMN);
            int goalColIndex = cursor.getColumnIndex(DbHelper.GOAL_COLUMN);
            int archivedIndex = cursor.getColumnIndex(DbHelper.ARCHIVED_COLUMN);
            int colorIndex = cursor.getColumnIndex(DbHelper.COLOR_COLUMN);

            do {
                // Read a account from DB
                Account account = new Account(cursor.getLong(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getLong(curSumColIndex),
                        cursor.getString(currencyColIndex),
                        cursor.getLong(decimalsColIndex),
                        cursor.getDouble(goalColIndex),
                        cursor.getInt(archivedIndex) != 0,
                        cursor.getInt(colorIndex));

                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }
}

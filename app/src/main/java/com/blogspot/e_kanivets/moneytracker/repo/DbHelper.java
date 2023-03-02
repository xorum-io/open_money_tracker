package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB Helper class.
 * Created on 29/08/14.
 *
 * @author Evgenii Kanivets
 */
public class DbHelper extends SQLiteOpenHelper {

    /* DB_VERSION = 1 */
    public static final String DB_NAME = "database";
    public static final int DB_VERSION = 6;
    public static final String TABLE_RECORDS = "records";
    public static final String TABLE_CATEGORIES = "categories";

    public static final String ID_COLUMN = "id";
    public static final String TIME_COLUMN = "time";
    public static final String TYPE_COLUMN = "type";
    public static final String TITLE_COLUMN = "title";
    public static final String CATEGORY_ID_COLUMN = "category_id";
    public static final String PRICE_COLUMN = "price";
    public static final String NAME_COLUMN = "name";

    /* DB_VERSION = 2 */
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String CURRENCY_COLUMN = "currency";

    public static final String ACCOUNT_ID_COLUMN = "account_id";
    public static final String CUR_SUM_COLUMN = "cur_sum";
    public static final String DEFAULT_ACCOUNT = "Default";
    public static final String DEFAULT_ACCOUNT_CURRENCY = "NON";

    public static final String TABLE_TRANSFERS = "transfers";
    public static final String FROM_ACCOUNT_ID_COLUMN = "from_account_id";
    public static final String TO_ACCOUNT_ID_COLUMN = "to_account_id";
    public static final String FROM_AMOUNT_COLUMN = "from_amount";
    public static final String TO_AMOUNT_COLUMN = "to_amount";
    public static final String CREATED_AT_COLUMN = "created_at";

    /* DB_VERSION = 3 */
    public static final String TABLE_RATES = "rates";
    public static final String FROM_CURRENCY_COLUMN = "from_currency";
    public static final String TO_CURRENCY_COLUMN = "to_currency";
    public static final String AMOUNT_COLUMN = "amount";

    /* DB_VERSION = 4 */
    public static final String DECIMALS_COLUMN = "decimals";
    public static final String DECIMALS_FROM_COLUMN = "decimals_from";
    public static final String DECIMALS_TO_COLUMN = "decimals_to";

    /* DB_VERSION = 5 */
    public static final String GOAL_COLUMN = "goal";
    public static final String ARCHIVED_COLUMN = "archived";
    public static final String COLOR_COLUMN = "color";

    /* DB_VERSION = 6 */
    public static final String NOTES_COLUMN = "notes";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //createDbVersion1(db);
        //createDbVersion2(db);
        //createDbVersion3(db);
        //createDbVersion4(db);
        //createDbVersion5(db);
        createDbVersion6(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.beginTransaction();

            db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                    + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CREATED_AT_COLUMN + " INTEGER,"
                    + TITLE_COLUMN + " TEXT,"
                    + CUR_SUM_COLUMN + " INTEGER,"
                    + CURRENCY_COLUMN + " TEXT );");

            /* Add account_id column into the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN " + ACCOUNT_ID_COLUMN + " INTEGER;");

            db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                    + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TIME_COLUMN + " INTEGER,"
                    + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                    + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                    + FROM_AMOUNT_COLUMN + " INTEGER,"
                    + TO_AMOUNT_COLUMN + " INTEGER);");

            /* Insert default account for all records from DB_VERSION = 1 */
            long id = insertDefaultAccount(db);

            /* Set the default account for all records from DB_VERSION = 1 */
            ContentValues contentValues = new ContentValues();
            contentValues.put(ACCOUNT_ID_COLUMN, id);
            db.update(DbHelper.TABLE_RECORDS, contentValues, null, null);

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        if (oldVersion < 3) {
            db.beginTransaction();

            createRatesTable(db);

            /* Add account_id column into the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN " + CURRENCY_COLUMN + " INTEGER;");

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        if (oldVersion < 4) {
            db.beginTransaction();

            /* Add decimals column into the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN "
                    + DECIMALS_COLUMN + " INTEGER;");

            /* Add decimals column into the accounts table */
            db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD COLUMN "
                    + DECIMALS_COLUMN + " INTEGER;");

            /* Add decimal_from and decimals_to columns into the transfers table */
            db.execSQL("ALTER TABLE " + TABLE_TRANSFERS + " ADD COLUMN "
                    + DECIMALS_FROM_COLUMN + " INTEGER;");
            db.execSQL("ALTER TABLE " + TABLE_TRANSFERS + " ADD COLUMN "
                    + DECIMALS_TO_COLUMN + " INTEGER;");

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        if (oldVersion < 5) {
            db.beginTransaction();

            /* Add goal column to the accounts table */
            db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD COLUMN "
                    + GOAL_COLUMN + " REAL;");

            /* Add archived flag column to the accounts table */
            db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD COLUMN "
                    + ARCHIVED_COLUMN + " INTEGER;");

            /* Add color column to the accounts table */
            db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD COLUMN "
                    + COLOR_COLUMN + " INTEGER;");

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        if (oldVersion < 6) {
            db.beginTransaction();

            /* Add notes column to the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN "
                    + NOTES_COLUMN + " TEXT;");
        }
    }

    @SuppressWarnings("unused")
    private void createDbVersion1(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");
    }

    @SuppressWarnings("unused")
    private void createDbVersion2(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT );");

        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER);");

        insertDefaultAccount(db);
    }

    @SuppressWarnings("unused")
    private void createDbVersion3(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT );");

        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER);");

        createRatesTable(db);

        insertDefaultAccount(db);
    }

    private void createDbVersion4(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER,"
                + DECIMALS_FROM_COLUMN + " INTEGER,"
                + DECIMALS_TO_COLUMN + " INTEGER);");

        createRatesTable(db);

        insertDefaultAccount(db);
    }

    private void createDbVersion5(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER,"
                + GOAL_COLUMN + " REAL,"
                + ARCHIVED_COLUMN + " INTEGER,"
                + COLOR_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER,"
                + DECIMALS_FROM_COLUMN + " INTEGER,"
                + DECIMALS_TO_COLUMN + " INTEGER);");

        createRatesTable(db);

        insertDefaultAccount(db);
    }

    private void createDbVersion6(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + NOTES_COLUMN + " TEXT,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT,"
                + DECIMALS_COLUMN + " INTEGER,"
                + GOAL_COLUMN + " REAL,"
                + ARCHIVED_COLUMN + " INTEGER,"
                + COLOR_COLUMN + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER,"
                + DECIMALS_FROM_COLUMN + " INTEGER,"
                + DECIMALS_TO_COLUMN + " INTEGER);");

        createRatesTable(db);

        insertDefaultAccount(db);
    }

    private void createRatesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RATES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + FROM_CURRENCY_COLUMN + " INTEGER,"
                + TO_CURRENCY_COLUMN + " INTEGER,"
                + AMOUNT_COLUMN + " REAL);");
    }

    private long insertDefaultAccount(SQLiteDatabase db) {
        /* Insert default account for all records from DB_VERSION = 1 */
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_COLUMN, DEFAULT_ACCOUNT);
        contentValues.put(CUR_SUM_COLUMN, 0);
        contentValues.put(CURRENCY_COLUMN, DEFAULT_ACCOUNT_CURRENCY);
        contentValues.put(CREATED_AT_COLUMN, System.currentTimeMillis());

        return db.insert(TABLE_ACCOUNTS, null, contentValues);
    }
}

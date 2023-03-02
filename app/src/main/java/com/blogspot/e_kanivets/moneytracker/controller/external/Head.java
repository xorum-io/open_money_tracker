package com.blogspot.e_kanivets.moneytracker.controller.external;

/**
 * Interface for head titles in import/export features.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
public interface Head {
    String TIME = "time";
    String TITLE = "title";
    String CATEGORY = "category";
    String NOTES = "notes";
    String PRICE = "price";
    String CURRENCY = "currency";
    String DELIMITER = ";";
    int COLUMN_COUNT = 7;
}

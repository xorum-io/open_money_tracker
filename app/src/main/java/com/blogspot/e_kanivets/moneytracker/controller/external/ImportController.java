package com.blogspot.e_kanivets.moneytracker.controller.external;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate import logic.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
public class ImportController {
    private final RecordController recordController;

    public ImportController(RecordController recordController) {
        this.recordController = recordController;
    }

    @NonNull
    public List<Record> importRecordsFromCsv(@Nullable String csv) {
        List<Record> recordList = new ArrayList<>();
        if (csv == null) return recordList;

        String lines[] = csv.split("\\r?\\n");
        for (String line : lines) {
            String[] words = line.split(Head.DELIMITER);
            if (words.length != Head.COLUMN_COUNT) continue;

            String timeCol = words[0];
            String titleCol = words[1];
            String categoryCol = words[2];
            String priceCol = words[3];
            String currencyCol = words[4];

            try {
                long time = Long.parseLong(timeCol);
                String title = titleCol.trim();
                String categoryName = categoryCol.trim();
                double price = Double.parseDouble(priceCol);
                String currency = currencyCol.trim();

                if (currency.length() != 3) continue;
                if (categoryName.isEmpty()) continue;

                int type;
                if (price < 0.0) type = Record.TYPE_EXPENSE;
                else type = Record.TYPE_INCOME;

                Category category = new Category(categoryName);
                Account account = new Account(-1, "MOCK", -1, currency, 0, -1, false, 0);

                Record record = new Record(time, type, title, category, Math.abs(price), account, currency);
                Record createdRecord = recordController.create(record);
                if (createdRecord != null) recordList.add(createdRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return recordList;
    }
}

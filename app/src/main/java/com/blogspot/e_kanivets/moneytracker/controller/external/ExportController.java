package com.blogspot.e_kanivets.moneytracker.controller.external;

import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate export logic.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
public class ExportController {
    private final RecordController recordController;
    private final CategoryController categoryController;

    public ExportController(RecordController recordController, CategoryController categoryController) {
        this.recordController = recordController;
        this.categoryController = categoryController;
    }

    public List<String> getRecordsForExport(long fromDate, long toDate) {
        List<String> result = new ArrayList<>();

        /* First of all add a header */
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append(Head.TIME).append(Head.DELIMITER);
        sb.append(Head.ACCOUNT_ID).append(Head.DELIMITER);
        sb.append(Head.TITLE).append(Head.DELIMITER);
        sb.append(Head.CATEGORY).append(Head.DELIMITER);
        sb.append(Head.NOTES).append(Head.DELIMITER);
        sb.append(Head.PRICE).append(Head.DELIMITER);
        sb.append(Head.CURRENCY);

        result.add(sb.toString());

        String condition = DbHelper.TIME_COLUMN + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(fromDate), Long.toString(toDate)};

        List<Record> recordList = recordController.readWithCondition(condition, args);

        for (Record record : recordList) {
            sb = new StringBuilder();
            sb.append(record.getTime()).append(Head.DELIMITER);

            long account_id = -1;
            if (record.getAccount() != null)
                account_id = record.getAccount().getId();

            sb.append(account_id).append(Head.DELIMITER);
            sb.append(record.getTitle()).append(Head.DELIMITER);

            Category category = null;
            if (record.getCategory() != null)
                category = categoryController.read(record.getCategory().getId());

            sb.append(category == null ? "NONE" : category.getName()).append(Head.DELIMITER);
            sb.append(record.getNotes()).append(Head.DELIMITER);
            sb.append(record.getType() == 0 ? record.getFullPrice()
                    : -record.getFullPrice()).append(Head.DELIMITER);
            sb.append(record.getCurrency() == null ? DbHelper.DEFAULT_ACCOUNT_CURRENCY
                    : record.getCurrency());

            result.add(sb.toString());
        }

        return result;
    }
}

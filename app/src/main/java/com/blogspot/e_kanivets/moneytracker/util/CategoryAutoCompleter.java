package com.blogspot.e_kanivets.moneytracker.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Util class to encapsulate category autocomplete logic.
 * Created on 3/18/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryAutoCompleter {

    private List<String> categoryList;
    private Map<String, String> recordTitleCategoryMap;

    @NonNull
    private final CategoryController categoryController;
    @NonNull
    private final PreferenceController preferenceController;

    public CategoryAutoCompleter(@NonNull CategoryController categoryController,
                                 @NonNull PreferenceController preferenceController) {
        this.categoryController = categoryController;
        this.preferenceController = preferenceController;

        categoryList = new ArrayList<>();
        recordTitleCategoryMap = preferenceController.readRecordTitleCategoryPairs();

        for (Category category : categoryController.readFiltered()) {
            categoryList.add(category.getName());
        }
    }

    public List<String> completeByPart(String part) {
        List<String> resultList = new ArrayList<>();

        for (String category : categoryList) {
            if (category.startsWith(part)) resultList.add(category);
        }

        return resultList;
    }

    public void removeFromAutoComplete(String category) {
        categoryList.remove(category);
        categoryController.disableCategory(category);
    }

    @Nullable
    public String completeByRecordTitle(String title) {
        return recordTitleCategoryMap.get(title);
    }

    public void addRecordTitleCategoryPair(String title, String category) {
        if (title.isEmpty() || category.isEmpty()) {
            return;
        }
        recordTitleCategoryMap.put(title, category);
        preferenceController.writeRecordTitleCategoryPairs(recordTitleCategoryMap);
    }
}

package com.blogspot.e_kanivets.moneytracker.controller.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller class to encapsulate category handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryController extends BaseController<Category> {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryController";

    @NonNull
    private final PreferenceController preferenceController;
    @NonNull
    private Set<String> filteredCategories;

    public CategoryController(@NonNull IRepo<Category> categoryRepo,
                              @NonNull PreferenceController preferenceController) {
        super(categoryRepo);
        this.preferenceController = preferenceController;
        filteredCategories = preferenceController.readFilteredCategories();
    }

    public Category readOrCreate(@Nullable String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) return null;

        enableCategory(categoryName);

        String condition = DbHelper.NAME_COLUMN + "=?";
        String[] args = {categoryName};
        List<Category> categoryList = repo.readWithCondition(condition, args);

        if (categoryList.size() >= 1) return categoryList.get(0);
        else return repo.create(new Category(categoryName));
    }

    @NonNull
    public List<Category> readFiltered() {
        List<Category> filteredList = new ArrayList<>();

        for (Category category : readAll()) {
            if (!filteredCategories.contains(category.getName())) filteredList.add(category);
        }

        return filteredList;
    }

    /**
     * @param category to disable when request filtered list.
     */
    public void disableCategory(@Nullable String category) {
        filteredCategories.add(category);
        preferenceController.writeFilteredCategories(filteredCategories);
    }

    /**
     * @param category to enable when request filtered list.
     */
    public void enableCategory(@Nullable String category) {
        filteredCategories.remove(category);
        preferenceController.writeFilteredCategories(filteredCategories);
    }
}

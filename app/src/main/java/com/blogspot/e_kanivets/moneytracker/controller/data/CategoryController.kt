package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * Controller class to encapsulate category handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
class CategoryController(
    categoryRepo: IRepo<Category>,
    private val preferenceController: PreferenceController
) : BaseController<Category>(categoryRepo) {

    private var filteredCategories: MutableSet<String> =
        preferenceController.readFilteredCategories().toMutableSet()

    fun readOrCreate(categoryName: String?): Category? {
        if (categoryName == null || categoryName.trim().isEmpty()) return null

        enableCategory(categoryName)

        val condition = "${DbHelper.NAME_COLUMN}=?"
        val args = arrayOf<String?>(categoryName)
        val categoryList = repo.readWithCondition(condition, args)

        return if (categoryList.size >= 1) categoryList[0]
        else repo.create(Category(categoryName))
    }

    fun readFiltered(): List<Category> {
        val filteredList = mutableListOf<Category>()

        for (category in readAll()) {
            if (!filteredCategories.contains(category.name)) filteredList.add(category)
        }

        return filteredList
    }

    /**
     * @param category to disable when request filtered list.
     */
    fun disableCategory(category: String?) {
        if (category != null) filteredCategories.add(category)
        preferenceController.writeFilteredCategories(filteredCategories)
    }

    /**
     * @param category to enable when request filtered list.
     */
    fun enableCategory(category: String?) {
        if (category != null) filteredCategories.remove(category)
        preferenceController.writeFilteredCategories(filteredCategories)
    }
}

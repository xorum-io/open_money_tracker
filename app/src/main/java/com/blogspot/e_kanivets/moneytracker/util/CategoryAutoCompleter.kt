package com.blogspot.e_kanivets.moneytracker.util

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController

/**
 * Util class to encapsulate category autocomplete logic.
 * Created on 3/18/16.
 *
 * @author Evgenii Kanivets
 */
class CategoryAutoCompleter(
    private val categoryController: CategoryController,
    private val preferenceController: PreferenceController
) {
    private val categoryList: MutableList<String> = mutableListOf()
    private val recordTitleCategoryMap: MutableMap<String, String> =
        preferenceController.readRecordTitleCategoryPairs().toMutableMap()

    init {
        for (category in categoryController.readFiltered()) {
            categoryList.add(category.name ?: continue)
        }
    }

    fun completeByPart(part: String): List<String> {
        val resultList = mutableListOf<String>()

        for (category in categoryList) {
            if (category.startsWith(part)) resultList.add(category)
        }

        return resultList
    }

    fun removeFromAutoComplete(category: String) {
        categoryList.remove(category)
        categoryController.disableCategory(category)
    }

    fun completeByRecordTitle(title: String): String? {
        return recordTitleCategoryMap[title]
    }

    fun addRecordTitleCategoryPair(title: String, category: String) {
        if (title.isEmpty() || category.isEmpty()) return
        recordTitleCategoryMap[title] = category
        preferenceController.writeRecordTitleCategoryPairs(recordTitleCategoryMap)
    }
}

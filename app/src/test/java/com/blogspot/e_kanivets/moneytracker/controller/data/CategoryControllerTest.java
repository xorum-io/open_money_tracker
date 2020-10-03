package com.blogspot.e_kanivets.moneytracker.controller.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryControllerTest {
    private PreferenceController mockPrefs;

    @Before
    public void setUp() throws Exception {
        mockPrefs = Mockito.mock(PreferenceController.class);
    }

    @Test
    public void testReadOrCreate() throws Exception {
        CategoryController categoryController = new CategoryController(emptyRepo, mockPrefs);

        assertNull(categoryController.readOrCreate(null));
        assertNull(categoryController.readOrCreate(""));

        Category result = categoryController.readOrCreate("category 1");
        assertEquals("category 1", result.getName());

        result = categoryController.readOrCreate("category 1");
        assertEquals("category 1", result.getName());

        categoryController = new CategoryController(repo, mockPrefs);

        result = categoryController.readOrCreate("category 1");
        assertEquals("category 1", result.getName());

        result = categoryController.readOrCreate("Category 1");
        assertEquals("Category 1", result.getName());
    }

    private IRepo<Category> emptyRepo = new IRepo<Category>() {
        @Nullable
        @Override
        public Category create(@Nullable Category instance) {
            return instance;
        }

        @Nullable
        @Override
        public Category read(long id) {
            return null;
        }

        @Nullable
        @Override
        public Category update(@Nullable Category instance) {
            return null;
        }

        @Override
        public boolean delete(@Nullable Category instance) {
            return false;
        }

        @NonNull
        @Override
        public List<Category> readAll() {
            return new ArrayList<>();
        }

        @NonNull
        @Override
        public List<Category> readWithCondition(@Nullable String condition, @Nullable String[] args) {
            return new ArrayList<>();
        }
    };

    private IRepo<Category> repo = new IRepo<Category>() {
        private Category category = new Category(1, "Category 1");

        @Nullable
        @Override
        public Category create(@Nullable Category instance) {
            return instance;
        }

        @Nullable
        @Override
        public Category read(long id) {
            if (id == 1) return category;
            else return null;
        }

        @Nullable
        @Override
        public Category update(@Nullable Category instance) {
            return null;
        }

        @Override
        public boolean delete(@Nullable Category instance) {
            return false;
        }

        @NonNull
        @Override
        public List<Category> readAll() {
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(category);
            return categories;
        }

        @NonNull
        @Override
        public List<Category> readWithCondition(@Nullable String condition, @Nullable String[] args) {
            return new ArrayList<>();
        }
    };
}

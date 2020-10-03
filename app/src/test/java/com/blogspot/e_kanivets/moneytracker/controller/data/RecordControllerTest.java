package com.blogspot.e_kanivets.moneytracker.controller.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordControllerTest {
    private RecordController recordController;
    private CategoryController categoryMock;
    private AccountController accountMock;
    private PreferenceController preferenceMock;
    private TestRepo repo;

    @Before
    public void setUp() throws Exception {
        repo = new TestRepo();
        categoryMock = Mockito.mock(CategoryController.class);
        accountMock = Mockito.mock(AccountController.class);
        preferenceMock = Mockito.mock(PreferenceController.class);

        recordController = new RecordController(repo, categoryMock, accountMock, preferenceMock);
    }

    @After
    public void tearDown() throws Exception {
        recordController = null;
        categoryMock = null;
        accountMock = null;
        repo = null;
    }

    @Test
    public void testCreate() throws Exception {
        assertNull(recordController.create(null));
        Mockito.verify(categoryMock, Mockito.times(0)).readOrCreate(null);
        Mockito.verify(accountMock, Mockito.times(0)).recordAdded(null);

        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        Record record = new Record(1, 1, Record.TYPE_INCOME, "r1", category, 10, account, "NON");
        Mockito.when(categoryMock.readOrCreate(category.getName())).thenReturn(category);

        Record result = recordController.create(record);
        assertEquals(result, record);
        assertEquals(repo.read(record.getId()), record);
        Mockito.verify(categoryMock, Mockito.times(1)).readOrCreate(category.getName());
        Mockito.verify(accountMock, Mockito.times(1)).recordAdded(record);
    }

    @Test
    public void testUpdate() throws Exception {
        assertNull(recordController.update(null));
        Mockito.verify(categoryMock, Mockito.times(0)).readOrCreate(null);
        Mockito.verify(accountMock, Mockito.times(0)).recordAdded(null);

        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        Record record = new Record(1, 1, Record.TYPE_INCOME, "r1", category, 10, account, "NON");
        Mockito.when(categoryMock.readOrCreate(category.getName())).thenReturn(category);

        Record result = recordController.update(record);
        assertEquals(result, record);
        assertEquals(repo.read(record.getId()), record);
        Mockito.verify(categoryMock, Mockito.times(1)).readOrCreate(category.getName());
        Mockito.verify(accountMock, Mockito.times(1)).recordUpdated(null, record);
    }

    @Test
    public void testDelete() throws Exception {
        assertFalse(recordController.delete(null));
        Mockito.verify(accountMock, Mockito.times(0)).recordDeleted(null);

        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        Record record = new Record(1, 1, Record.TYPE_INCOME, "r1", category, 10, account, "NON");

        assertFalse(recordController.delete(record));
        Mockito.verify(accountMock, Mockito.times(0)).recordDeleted(null);

        repo.create(record);
        Mockito.when(recordController.delete(record)).thenReturn(true);
        assertTrue(recordController.delete(record));
        Mockito.verify(accountMock, Mockito.times(2)).recordDeleted(record);
    }

    @Test
    public void testRead() throws Exception {
        assertNull(recordController.read(-1));

        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        Record recordNotFull = new Record(1, 1, Record.TYPE_INCOME, "r1", category.getId(), 10,
                account.getId(), "NON", 0);
        Record record = new Record(1, 1, Record.TYPE_INCOME, "r1", category, 10, account, "NON");

        repo.create(recordNotFull);
        Mockito.when(categoryMock.read(category.getId())).thenReturn(category);
        Mockito.when(accountMock.read(account.getId())).thenReturn(account);

        assertEquals(record, recordController.read(record.getId()));
        Mockito.verify(categoryMock, Mockito.times(1)).read(category.getId());
        Mockito.verify(accountMock, Mockito.times(1)).read(account.getId());
    }

    @Test
    public void testReadAll() throws Exception {
        assertEquals(repo.readAll(), recordController.readAll());
    }

    @Test
    public void testReadWithCondition() throws Exception {
        assertEquals(new ArrayList<Record>(), recordController.readWithCondition(null, null));
    }

    @Test
    public void testGetRecordsForPeriod() throws Exception {
        assertEquals(new ArrayList<Record>(), recordController.getRecordsForPeriod(
                new Period(new Date(), new Date(), Period.TYPE_CUSTOM)));
    }

    private class TestRepo implements IRepo<Record> {
        private Map<Long, Record> recordHashMap = new HashMap<>();

        @Nullable
        @Override
        public Record create(@Nullable Record instance) {
            if (instance == null) return null;
            recordHashMap.put(instance.getId(), instance);
            return instance;
        }

        @Nullable
        @Override
        public Record read(long id) {
            return recordHashMap.get(id);
        }

        @Nullable
        @Override
        public Record update(@Nullable Record instance) {
            if (instance == null) return null;
            recordHashMap.put(instance.getId(), instance);
            return instance;
        }

        @Override
        public boolean delete(@Nullable Record instance) {
            if (instance == null) return false;
            recordHashMap.remove(instance.getId());
            return true;
        }

        @NonNull
        @Override
        public List<Record> readAll() {
            List<Record> recordList = new ArrayList<>();
            for (long key : recordHashMap.keySet()) {
                recordList.add(recordHashMap.get(key));
            }

            Collections.sort(recordList, new Comparator<Record>() {
                @Override
                public int compare(Record lhs, Record rhs) {
                    return lhs.getTime() < rhs.getTime() ? -1 : (lhs.getTime() == rhs.getTime() ? 0 : 1);
                }
            });

            return recordList;
        }

        @NonNull
        @Override
        public List<Record> readWithCondition(@Nullable String condition, @Nullable String[] args) {
            return readAll();
        }
    }
}

package com.blogspot.e_kanivets.moneytracker.controller.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/22/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountControllerTest {
    private AccountController accountController;
    private PreferenceController mockPrefs;
    private TestRepo repo;

    @Before
    public void setUp() throws Exception {
        repo = new TestRepo();
        mockPrefs = Mockito.mock(PreferenceController.class);
        accountController = new AccountController(repo, mockPrefs);
    }

    @After
    public void tearDown() throws Exception {
        repo = null;
        mockPrefs = null;
        accountController = null;
    }

    @Test
    public void testRecordAdded() throws Exception {
        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        repo.create(account);

        Record income = new Record(1, 1, Record.TYPE_INCOME, "income", category, 10, account,
                account.getCurrency());

        accountController.recordAdded(income);
        assertEquals(110, account.getCurSum());

        Record expense = new Record(1, 1, Record.TYPE_EXPENSE, "expense", category, 10, account,
                account.getCurrency());

        accountController.recordAdded(expense);
        assertEquals(100, account.getCurSum());

        accountController.recordAdded(null);
        assertEquals(100, account.getCurSum());

        Record broken = new Record(1, 1, -1, "expense", category, 10, account, account.getCurrency());
        accountController.recordAdded(broken);
        assertEquals(100, account.getCurSum());
    }

    @Test
    public void testRecordDeleted() throws Exception {
        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        repo.create(account);

        Record income = new Record(1, 1, Record.TYPE_INCOME, "income", category, 10, account,
                account.getCurrency());

        accountController.recordDeleted(income);
        assertEquals(90, account.getCurSum());

        Record expense = new Record(1, 1, Record.TYPE_EXPENSE, "expense", category, 10, account,
                account.getCurrency());

        accountController.recordDeleted(expense);
        assertEquals(100, account.getCurSum());

        accountController.recordAdded(null);
        assertEquals(100, account.getCurSum());

        Record broken = new Record(1, 1, -1, "expense", category, 10, account, account.getCurrency());
        accountController.recordAdded(broken);
        assertEquals(100, account.getCurSum());
    }

    @Test
    public void testRecordUpdated() throws Exception {
        Category category = new Category(1, "c1");
        Account account = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        repo.create(account);

        Record incomeOld = new Record(1, 1, Record.TYPE_INCOME, "income", category, 10, account,
                account.getCurrency());
        Record incomeNew = new Record(1, 1, Record.TYPE_INCOME, "income", category, 100, account,
                account.getCurrency());

        accountController.recordUpdated(incomeOld, incomeNew);
        assertEquals(190, account.getCurSum());

        accountController.recordUpdated(incomeNew, incomeOld);
        assertEquals(100, account.getCurSum());

        Record expenseOld = new Record(1, 1, Record.TYPE_EXPENSE, "expense", category, 10, account,
                account.getCurrency());
        Record expenseNew = new Record(1, 1, Record.TYPE_EXPENSE, "expense", category, 100, account,
                account.getCurrency());

        accountController.recordUpdated(expenseOld, expenseNew);
        assertEquals(10, account.getCurSum());

        accountController.recordUpdated(expenseNew, expenseOld);
        assertEquals(100, account.getCurSum());

        accountController.recordUpdated(null, null);
        assertEquals(100, account.getCurSum());

        Record broken = new Record(1, 1, -1, "expense", category, 10, account, account.getCurrency());
        accountController.recordUpdated(broken, broken);
        assertEquals(100, account.getCurSum());
    }

    @Test
    public void testTransferDone() throws Exception {
        Account account1 = new Account(1, "a1", 100, "NON", 0, 0, false, 0);
        Account account2 = new Account(2, "a2", 0, "NON", 0, 0, false, 0);

        repo.create(account1);
        repo.create(account2);

        accountController.transferDone(null);
        assertEquals(100, account1.getCurSum());
        assertEquals(0, account2.getCurSum());

        Transfer transfer = new Transfer(1, 1, account1.getId(), account2.getId(), 10, 10, 0, 0);
        accountController.transferDone(transfer);
        assertEquals(90, account1.getCurSum());
        assertEquals(10, account2.getCurSum());

        transfer = new Transfer(2, 1, account1.getId(), account2.getId(), 10, 10, 0, 0);
        accountController.transferDone(transfer);
        assertEquals(80, account1.getCurSum());
        assertEquals(20, account2.getCurSum());

        transfer = new Transfer(2, 1, account2.getId(), account1.getId(), 20, 20, 0, 0);
        accountController.transferDone(transfer);
        assertEquals(100, account1.getCurSum());
        assertEquals(0, account2.getCurSum());

        transfer = new Transfer(2, 1, account1.getId(), account2.getId(), 0, 100, 0, 0);
        accountController.transferDone(transfer);
        assertEquals(100, account1.getCurSum());
        assertEquals(100, account2.getCurSum());
    }

    @Test
    public void testReadDefaultAccount() throws Exception {
        assertNull(accountController.readDefaultAccount());

        Account account1 = new Account(1, "a1", 100, "UAH", 0, 0, false, 0);
        repo.create(account1);
        assertEquals(account1, accountController.readDefaultAccount());

        Account account2 = new Account(2, "a2", 0, "UAH", 0, 0, false, 0);
        repo.create(account2);
        assertEquals(account1, accountController.readDefaultAccount());

        Mockito.when(mockPrefs.readDefaultAccountId()).thenReturn(2L);
        assertEquals(account2, accountController.readDefaultAccount());

        Mockito.when(mockPrefs.readDefaultAccountId()).thenReturn(-1L);
        assertEquals(account1, accountController.readDefaultAccount());
    }

    private class TestRepo implements IRepo<Account> {
        private Map<Long, Account> accountHashMap = new HashMap<>();

        @Nullable
        @Override
        public Account create(@Nullable Account instance) {
            if (instance == null) return null;
            accountHashMap.put(instance.getId(), instance);
            return instance;
        }

        @Nullable
        @Override
        public Account read(long id) {
            return accountHashMap.get(id);
        }

        @Nullable
        @Override
        public Account update(@Nullable Account instance) {
            if (instance == null) return null;
            accountHashMap.put(instance.getId(), instance);
            return instance;
        }

        @Override
        public boolean delete(@Nullable Account instance) {
            if (instance == null) return false;
            accountHashMap.remove(instance.getId());
            return true;
        }

        @NonNull
        @Override
        public List<Account> readAll() {
            List<Account> recordList = new ArrayList<>();
            for (long key : accountHashMap.keySet()) {
                recordList.add(accountHashMap.get(key));
            }

            Collections.sort(recordList, new Comparator<Account>() {
                @Override
                public int compare(Account lhs, Account rhs) {
                    return lhs.getTitle().compareTo(rhs.getTitle());
                }
            });

            return recordList;
        }

        @NonNull
        @Override
        public List<Account> readWithCondition(@Nullable String condition, @Nullable String[] args) {
            return readAll();
        }
    }
}

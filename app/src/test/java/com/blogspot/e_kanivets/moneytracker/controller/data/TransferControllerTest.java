package com.blogspot.e_kanivets.moneytracker.controller.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class TransferControllerTest {

    @Test
    public void testCreate() throws Exception {
        AccountController mock = Mockito.mock(AccountController.class);
        Transfer transfer = new Transfer(1, 1, 1, 2, 10, 20, 0, 0);

        Mockito.when(mock.transferDone(transfer)).thenReturn(true);
        Mockito.when(mock.transferDone(null)).thenReturn(false);

        TransferController transferController = new TransferController(repo, mock);

        assertEquals(transfer, transferController.create(transfer));
        Mockito.verify(mock, Mockito.times(1)).transferDone(transfer);

        assertNull(transferController.create(null));
        Mockito.verify(mock, Mockito.times(0)).transferDone(null);
    }

    private IRepo<Transfer> repo = new IRepo<Transfer>() {
        @Nullable
        @Override
        public Transfer create(@Nullable Transfer instance) {
            return instance;
        }

        @Nullable
        @Override
        public Transfer read(long id) {
            return null;
        }

        @Nullable
        @Override
        public Transfer update(@Nullable Transfer instance) {
            return null;
        }

        @Override
        public boolean delete(@Nullable Transfer instance) {
            return false;
        }

        @NonNull
        @Override
        public List<Transfer> readAll() {
            return null;
        }

        @NonNull
        @Override
        public List<Transfer> readWithCondition(@Nullable String condition, @Nullable String[] args) {
            return null;
        }
    };
}

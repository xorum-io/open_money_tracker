package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks;

import android.os.AsyncTask;
import androidx.annotation.Nullable;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DropboxFetchBackupListAsyncTask extends AsyncTask<Void, List<String>, List<String>> {
    private DbxClientV2 dbClient;

    @Nullable
    private BackupController.OnBackupListener listener;

    public DropboxFetchBackupListAsyncTask(DbxClientV2 dbClient,
            @Nullable BackupController.OnBackupListener listener) {
        this.dbClient = dbClient;
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<Metadata> entryList = new ArrayList<>();
        List<String> backupList = new ArrayList<>();

        try {
            entryList = dbClient.files().listFolder("").getEntries();
        } catch (DbxException e) {
            e.printStackTrace();
        }

        for (Metadata entry : entryList) {
            backupList.add(entry.getName());
        }

        return backupList;
    }

    @Override
    protected void onPostExecute(List<String> backupList) {
        super.onPostExecute(backupList);
        if (listener == null) return;

        Collections.reverse(backupList);
        listener.onBackupsFetched(backupList);
    }
}

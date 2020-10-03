package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DropboxBackupAsyncTask extends AsyncTask<Void, String, String> {

    private DbxClientV2 dbClient;
    private String appDbFileName;
    private String fileName;

    @Nullable private BackupController.OnBackupListener listener;

    public DropboxBackupAsyncTask(@NonNull DbxClientV2 dbClient, String fileName, String appDbFileName,
            @Nullable BackupController.OnBackupListener listener) {
        this.dbClient = dbClient;
        this.fileName = fileName;
        this.appDbFileName = appDbFileName;
        this.listener = listener;
    }

    @Override protected String doInBackground(Void... params) {
        FileInputStream fileInputStream = readAppDb();
        if (fileInputStream == null) return null;

        FileMetadata info = null;

        try {
            info = dbClient.files().upload("/" + fileName).uploadAndFinish(fileInputStream);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        } else {
            return BackupController.OnBackupListener.SUCCESS;
        }
    }

    @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener == null) return;

        if (BackupController.OnBackupListener.SUCCESS.equals(result)) {
            listener.onBackupSuccess();
        } else {
            listener.onBackupFailure(result);
        }
    }

    @Nullable private FileInputStream readAppDb() {
        File dbFile = new File(appDbFileName);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(dbFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fis;
    }
}

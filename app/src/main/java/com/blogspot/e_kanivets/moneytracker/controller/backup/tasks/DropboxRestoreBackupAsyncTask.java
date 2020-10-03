package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DropboxRestoreBackupAsyncTask extends AsyncTask<Void, String, String> {

    private DbxClientV2 dbClient;
    private String appDbFileName;
    private String backupName;

    @Nullable private BackupController.OnBackupListener listener;

    public DropboxRestoreBackupAsyncTask(DbxClientV2 dbClient, String appDbFileName, String backupName,
            @Nullable BackupController.OnBackupListener listener) {
        this.dbClient = dbClient;
        this.appDbFileName = appDbFileName;
        this.backupName = backupName;
        this.listener = listener;
    }

    @Override protected String doInBackground(Void... params) {
        final File file = new File(getRestoreFileName());
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (outputStream == null) {
            return null;
        } else {
            FileMetadata info = null;
            try {
                info = dbClient.files().download("/" + backupName).download(outputStream);
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }

            if (info == null) {
                return null;
            } else {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                if (file.exists() && file.length() != 0) {
                    boolean renamed = file.renameTo(new File(appDbFileName));
                    return renamed ? BackupController.OnBackupListener.SUCCESS : null;
                } else {
                    return null;
                }
            }
        }
    }

    @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener == null) return;

        if (BackupController.OnBackupListener.SUCCESS.equals(result)) {
            listener.onRestoreSuccess(backupName);
        } else {
            listener.onRestoreFailure(result);
        }
    }

    @NonNull private String getRestoreFileName() {
        return appDbFileName + ".restore";
    }
}

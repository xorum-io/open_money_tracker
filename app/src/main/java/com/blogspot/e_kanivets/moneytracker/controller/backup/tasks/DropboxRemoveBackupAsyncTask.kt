package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks

import android.os.AsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2

class DropboxRemoveBackupAsyncTask(
    private val dbClient: DbxClientV2,
    private val backupName: String,
    private val listener: BackupController.OnBackupListener?
) : AsyncTask<Void, String, String>() {

    override fun doInBackground(vararg params: Void): String? {
        return try {
            val metadata = dbClient.files().deleteV2("/$backupName").metadata
            if (metadata == null) null else BackupController.OnBackupListener.SUCCESS
        } catch (e: DbxException) {
            e.printStackTrace()
            e.message
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (listener == null) return

        if (BackupController.OnBackupListener.SUCCESS == result) {
            listener.onRemoveSuccess()
        } else {
            listener.onRemoveFailure(result)
        }
    }
}

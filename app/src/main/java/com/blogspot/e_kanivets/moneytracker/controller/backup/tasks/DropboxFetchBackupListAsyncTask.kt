package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks

import android.os.AsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2

class DropboxFetchBackupListAsyncTask(
    private val dbClient: DbxClientV2,
    private val listener: BackupController.OnBackupListener?
) : AsyncTask<Void, List<String>, List<String>>() {

    override fun doInBackground(vararg params: Void): List<String> {
        val backupList = mutableListOf<String>()

        try {
            val entryList = dbClient.files().listFolder("").entries
            for (entry in entryList) {
                backupList.add(entry.name)
            }
        } catch (e: DbxException) {
            e.printStackTrace()
        }

        return backupList
    }

    override fun onPostExecute(backupList: List<String>?) {
        super.onPostExecute(backupList)
        if (listener == null) return

        val result = backupList?.reversed() ?: emptyList()
        listener.onBackupsFetched(result)
    }
}

package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks

import android.os.AsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DropboxRestoreBackupAsyncTask(
    private val dbClient: DbxClientV2,
    private val appDbFileName: String,
    private val backupName: String,
    private val listener: BackupController.OnBackupListener?
) : AsyncTask<Void, String, String>() {

    override fun doInBackground(vararg params: Void): String? {
        val file = File(getRestoreFileName())
        val outputStream = try {
            FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } ?: return null

        val info = try {
            dbClient.files().download("/$backupName").download(outputStream)
        } catch (e: DbxException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        if (info == null) return null

        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return if (file.exists() && file.length() != 0L) {
            val renamed = file.renameTo(File(appDbFileName))
            if (renamed) BackupController.OnBackupListener.SUCCESS else null
        } else {
            null
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (listener == null) return

        if (BackupController.OnBackupListener.SUCCESS == result) {
            listener.onRestoreSuccess(backupName)
        } else {
            listener.onRestoreFailure(result)
        }
    }

    private fun getRestoreFileName(): String = "$appDbFileName.restore"
}

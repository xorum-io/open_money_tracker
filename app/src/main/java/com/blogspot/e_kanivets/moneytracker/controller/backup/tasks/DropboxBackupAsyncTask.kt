package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks

import android.os.AsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class DropboxBackupAsyncTask(
    private val dbClient: DbxClientV2,
    private val fileName: String,
    private val appDbFileName: String,
    private val listener: BackupController.OnBackupListener?
) : AsyncTask<Void, String, String>() {

    override fun doInBackground(vararg params: Void): String? {
        val fileInputStream = readAppDb() ?: return null

        val info = try {
            dbClient.files().upload("/$fileName").uploadAndFinish(fileInputStream)
        } catch (e: DbxException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        return if (info == null) null else BackupController.OnBackupListener.SUCCESS
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (listener == null) return

        if (BackupController.OnBackupListener.SUCCESS == result) {
            listener.onBackupSuccess()
        } else {
            listener.onBackupFailure(result)
        }
    }

    private fun readAppDb(): FileInputStream? {
        val dbFile = File(appDbFileName)
        return try {
            FileInputStream(dbFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}

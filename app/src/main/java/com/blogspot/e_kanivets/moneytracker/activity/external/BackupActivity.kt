package com.blogspot.e_kanivets.moneytracker.activity.external

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.BackupAdapter
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityBackupBinding
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.DbxClientV2
import javax.inject.Inject
import timber.log.Timber

class BackupActivity : BaseBackActivity(),
        BackupAdapter.OnBackupListener, BackupController.OnBackupListener {
    companion object {
        private const val APP_KEY = "5lqugcckdy9y6lj"
    }

    @Inject lateinit var preferenceController: PreferenceController
    @Inject lateinit var backupController: BackupController

    private lateinit var dbClient: DbxClientV2

    private lateinit var binding: ActivityBackupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this)

        val accessToken = preferenceController.readDropboxAccessToken()
        if (accessToken == null) {
            Auth.startOAuth2Authentication(this, APP_KEY)
        } else {
            val config = DbxRequestConfig("open_money_tracker")
            dbClient = DbxClientV2(config, accessToken)
            backupController.onBackupListener = this
            fetchBackups()
        }

        return true
    }

    private fun initViews() {
        binding.btnBackupNow.isEnabled = preferenceController.readDropboxAccessToken() != null
        binding.btnBackupNow.setOnClickListener { backupNow() }
        binding.listView.setOnItemClickListener { _, _, position, _ -> restoreBackupClicked(position) }
    }

    override fun onResume() {
        super.onResume()

        Auth.getOAuth2Token()?.let { token ->
            try {
                preferenceController.writeDropboxAccessToken(token)
                binding.btnBackupNow.isEnabled = true
                val config = DbxRequestConfig("open_money_tracker")
                dbClient = DbxClientV2(config, token)
                fetchBackups()
            } catch (e: IllegalStateException) {
                Timber.e("Error authenticating: %s", e.message)
            }
        }
    }

    override fun onBackupDelete(backupName: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.delete_backup_title))
            setMessage(getString(R.string.delete_backup_message, backupName))
            setPositiveButton(android.R.string.ok) { _, _ -> removeBackup(backupName) }
            setNegativeButton(android.R.string.cancel, null)
        }.show()
    }

    override fun onBackupsFetched(backupList: List<String>) {
        if (isFinishing) return

        stopProgress()

        val backupAdapter = BackupAdapter(this, backupList)
        backupAdapter.onBackupListener = this
        binding.listView.adapter = backupAdapter
    }

    override fun onBackupSuccess() {
        CrashlyticsProxy.get().logEvent("Backup success")
        Timber.d("Backup success.")
        if (isFinishing) return

        stopProgress()
        fetchBackups()
    }

    override fun onBackupFailure(reason: String?) {
        CrashlyticsProxy.get().logEvent("Backup failure")
        Timber.d("Backup failure.")
        if (isFinishing) return

        stopProgress()
        showToast(R.string.failed_create_backup)

        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION == reason) logout()
    }

    override fun onRestoreSuccess(backupName: String) {
        CrashlyticsProxy.get().logEvent("Restore Success")
        Timber.d("Restore success.")
        if (isFinishing) return

        stopProgress()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.backup_is_restored))
        builder.setMessage(getString(R.string.backup_restored, backupName))
        builder.setOnDismissListener {
            MtApp.get().buildAppComponent()
            setResult(RESULT_OK)
            finish()
        }
        builder.setPositiveButton(android.R.string.ok, null)
        builder.show()
    }

    override fun onRestoreFailure(reason: String?) {
        CrashlyticsProxy.get().logEvent("Restore Failure")
        Timber.d("Restore failure.")
        if (isFinishing) return

        stopProgress()
        showToast(R.string.failed_restore_backup)

        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION == reason) logout()
    }

    override fun onRemoveSuccess() {
        CrashlyticsProxy.get().logEvent("Remove Success")
        Timber.d("Remove success.")
        if (isFinishing) return

        stopProgress()
        fetchBackups()
    }

    override fun onRemoveFailure(reason: String?) {
        CrashlyticsProxy.get().logEvent("Remove Failure")
        Timber.d("Remove failure.")
        if (isFinishing) return

        stopProgress()
        showToast(reason)
    }

    fun backupNow() {
        CrashlyticsProxy.get().logButton("Make Backup")
        startProgress(getString(R.string.making_backup))
        backupController.makeBackup(dbClient)
    }

    fun restoreBackupClicked(position: Int) {
        CrashlyticsProxy.get().logButton("Restore backup")
        val backupName = binding.listView.adapter.getItem(position).toString()

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.warning))
            setMessage(getString(R.string.want_erase_and_restore, backupName))
            setPositiveButton(android.R.string.ok) { _, _ -> restoreBackup(backupName) }
            setNegativeButton(android.R.string.cancel, null)
        }.show()
    }

    private fun restoreBackup(backupName: String) {
        startProgress(getString(R.string.restoring_backup))
        backupController.restoreBackup(dbClient, backupName)
    }

    private fun fetchBackups() {
        startProgress(getString(R.string.fetching_backups))
        backupController.fetchBackups(dbClient)
    }

    private fun removeBackup(backupName: String) {
        startProgress(getString(R.string.removing_backup))
        backupController.removeBackup(dbClient, backupName)
    }

    private fun logout() {
        preferenceController.writeDropboxAccessToken(null)
        Auth.startOAuth2Authentication(this, APP_KEY)
        binding.btnBackupNow.isEnabled = false
    }
}

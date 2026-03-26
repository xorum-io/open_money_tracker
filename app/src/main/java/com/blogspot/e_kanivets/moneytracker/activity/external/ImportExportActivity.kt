package com.blogspot.e_kanivets.moneytracker.activity.external

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.controller.external.ExportController
import com.blogspot.e_kanivets.moneytracker.controller.external.ImportController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityImportExportBinding
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import org.koin.android.ext.android.inject
import timber.log.Timber

class ImportExportActivity : BaseBackActivity() {

    companion object {
        private const val DEFAULT_EXPORT_FILE_NAME = "money_tracker.csv"
    }

    private val importController: ImportController by inject()
    private val exportController: ExportController by inject()

    private lateinit var binding: ActivityImportExportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportExportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initViews()
    }

    private fun initViews() {
        binding.btnImport.setOnClickListener { importRecords() }
        binding.btnExport.setOnClickListener { exportRecords() }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_import, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == R.id.action_help) {
            showHelp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showHelp() {
        CrashlyticsProxy.instance.logButton("Show Help")
        AlertDialog.Builder(this)
            .setTitle(R.string.help)
            .setMessage(R.string.import_help)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    fun importRecords() {
        CrashlyticsProxy.instance.logButton("Import Records")
        val data = binding.etImportData.text.toString().trim()
        CoroutineScope(Dispatchers.Main).launch {
            startProgress(getString(R.string.importing_records))
            val recordCount = withContext(Dispatchers.IO) {
                val recordList = importController.importRecordsFromCsv(data)
                recordList.size
            }
            stopProgress()
            showToast(getString(R.string.records_imported, recordCount.toString()))
            setResult(RESULT_OK)
        }
    }

    fun exportRecords() {
        CrashlyticsProxy.instance.logButton("Export Records")
        val records = exportController.getRecordsForExport(0, Long.MAX_VALUE)
        val exportDir = File(cacheDir, "export")
        val exportDirCreated = exportDir.mkdirs()
        Timber.d("ExportDirCreated: %b", exportDirCreated)
        val outFile = if (exportDir.exists()) File(exportDir, DEFAULT_EXPORT_FILE_NAME) else return
        var pw: PrintWriter? = null
        try {
            pw = PrintWriter(outFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (pw != null) {
            for (record in records) {
                pw.println(record)
                pw.flush()
            }
            pw.flush()
            pw.close()
            shareExportedRecords(outFile)
        }
    }

    private fun shareExportedRecords(exportFile: File) {
        CrashlyticsProxy.instance.logEvent("Share Records")
        val fileUri: Uri = FileProvider.getUriForFile(this, packageName, exportFile)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share exported records"))
    }
}

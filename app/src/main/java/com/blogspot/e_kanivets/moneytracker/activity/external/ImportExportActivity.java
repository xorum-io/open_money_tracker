package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.external.ExportController;
import com.blogspot.e_kanivets.moneytracker.controller.external.ImportController;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityImportExportBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ImportExportActivity extends BaseBackActivity {
    private static final String DEFAULT_EXPORT_FILE_NAME = "money_tracker.csv";

    @Inject
    ImportController importController;
    @Inject
    ExportController exportController;

    private ActivityImportExportBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityImportExportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initToolbar();
        initViews();
    }

    private void initViews() {
        binding.btnImport.setOnClickListener(view -> importRecords());
        binding.btnExport.setOnClickListener(view -> exportRecords());
    }

    private boolean initData() {
        getAppComponent().inject(ImportExportActivity.this);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_import, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHelp() {
        CrashlyticsProxy.get().logButton("Show Help");
        AlertDialog.Builder builder = new AlertDialog.Builder(ImportExportActivity.this);
        builder.setTitle(R.string.help)
                .setMessage(R.string.import_help)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void importRecords() {
        CrashlyticsProxy.get().logButton("Import Records");
        final String data = binding.etImportData.getText().toString().trim();

        AsyncTask<Void, Void, Integer> importTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startProgress(getString(R.string.importing_records));
            }

            @Override
            protected Integer doInBackground(Void... params) {
                List<Record> recordList = importController.importRecordsFromCsv(data);
                return recordList.size();
            }

            @Override
            protected void onPostExecute(Integer recordCount) {
                super.onPostExecute(recordCount);
                stopProgress();
                showToast(getString(R.string.records_imported, recordCount.toString()));
                setResult(RESULT_OK);
            }
        };
        importTask.execute();
    }

    public void exportRecords() {
        CrashlyticsProxy.get().logButton("Export Records");
        List<String> records = exportController.getRecordsForExport(0, Long.MAX_VALUE);

        File exportDir = new File(getCacheDir(), "export");
        boolean exportDirCreated = exportDir.mkdirs();
        Timber.d("ExportDirCreated: %b", exportDirCreated);

        File outFile;
        if (exportDir.exists()) outFile = new File(exportDir, DEFAULT_EXPORT_FILE_NAME);
        else return;

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (pw != null) {
            for (String record : records) {
                pw.println(record);
                pw.flush();
            }

            pw.flush();
            pw.close();

            shareExportedRecords(outFile);
        }
    }

    private void shareExportedRecords(@NonNull File exportFile) {
        CrashlyticsProxy.get().logEvent("Share Records");
        Uri fileUri = FileProvider.getUriForFile(ImportExportActivity.this, getPackageName(), exportFile);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share exported records"));
    }
}

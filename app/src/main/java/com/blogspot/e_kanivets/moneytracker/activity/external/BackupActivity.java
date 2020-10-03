package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.BackupAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

public class BackupActivity extends BaseBackActivity
        implements BackupAdapter.OnBackupListener, BackupController.OnBackupListener {
    private static final String APP_KEY = "5lqugcckdy9y6lj";

    @Inject PreferenceController preferenceController;
    @Inject BackupController backupController;

    private DbxClientV2 dbClient;

    @BindView(R.id.btn_backup_now) View btnBackupNow;
    @BindView(R.id.listView) ListView listView;

    @Override protected int getContentViewId() {
        return R.layout.activity_backup;
    }

    @Override protected boolean initData() {
        getAppComponent().inject(BackupActivity.this);

        String accessToken = preferenceController.readDropboxAccessToken();
        if (accessToken == null) {
            Auth.startOAuth2Authentication(BackupActivity.this, APP_KEY);
        } else {
            DbxRequestConfig config = new DbxRequestConfig("open_money_tracker");
            dbClient = new DbxClientV2(config, accessToken);
            backupController.setOnBackupListener(this);
            fetchBackups();
        }

        return super.initData();
    }

    @Override protected void initViews() {
        super.initViews();
        btnBackupNow.setEnabled(preferenceController.readDropboxAccessToken() != null);
    }

    @Override protected void onResume() {
        super.onResume();

        if (Auth.getOAuth2Token() != null) {
            try {
                preferenceController.writeDropboxAccessToken(Auth.getOAuth2Token());
                btnBackupNow.setEnabled(true);
                DbxRequestConfig config = new DbxRequestConfig("open_money_tracker");
                dbClient = new DbxClientV2(config, Auth.getOAuth2Token());
                fetchBackups();
            } catch (IllegalStateException e) {
                Timber.e("Error authenticating: %s", e.getMessage());
            }
        }
    }

    @Override public void onBackupDelete(@NotNull final String backupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
        builder.setTitle(getString(R.string.delete_backup_title));
        builder.setMessage(getString(R.string.delete_backup_message, backupName));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                removeBackup(backupName);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @Override public void onBackupsFetched(@NonNull List<String> backupList) {
        if (isFinishing()) return;

        stopProgress();

        BackupAdapter backupAdapter = new BackupAdapter(BackupActivity.this, backupList);
        backupAdapter.setOnBackupListener(BackupActivity.this);
        listView.setAdapter(backupAdapter);
    }

    @Override public void onBackupSuccess() {
        CrashlyticsProxy.get().logEvent("Backup success");
        Timber.d("Backup success.");
        if (isFinishing()) return;

        stopProgress();
        fetchBackups();
    }

    @Override public void onBackupFailure(String reason) {
        CrashlyticsProxy.get().logEvent("Backup failure");
        Timber.d("Backup failure.");
        if (isFinishing()) return;

        stopProgress();
        showToast(R.string.failed_create_backup);

        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION.equals(reason)) logout();
    }

    @Override public void onRestoreSuccess(@NonNull String backupName) {
        CrashlyticsProxy.get().logEvent("Restore Success");
        Timber.d("Restore success.");
        if (isFinishing()) return;

        stopProgress();

        AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
        builder.setTitle(getString(R.string.backup_is_restored));
        builder.setMessage(getString(R.string.backup_restored, backupName));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                MtApp.get().buildAppComponent();
                setResult(RESULT_OK);
                finish();
            }
        });
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    @Override public void onRestoreFailure(String reason) {
        CrashlyticsProxy.get().logEvent("Restore Failure");
        Timber.d("Restore failure.");
        if (isFinishing()) return;

        stopProgress();
        showToast(R.string.failed_restore_backup);

        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION.equals(reason)) logout();
    }

    @Override public void onRemoveSuccess() {
        CrashlyticsProxy.get().logEvent("Remove Success");
        Timber.d("Remove success.");
        if (isFinishing()) return;

        stopProgress();
        fetchBackups();
    }

    @Override public void onRemoveFailure(@Nullable String reason) {
        CrashlyticsProxy.get().logEvent("Remove Failure");
        Timber.d("Remove failure.");
        if (isFinishing()) return;

        stopProgress();
        showToast(reason);
    }

    @OnClick(R.id.btn_backup_now) public void backupNow() {
        CrashlyticsProxy.get().logButton("Make Backup");
        startProgress(getString(R.string.making_backup));
        backupController.makeBackup(dbClient);
    }

    @OnItemClick(R.id.listView) public void restoreBackupClicked(int position) {
        CrashlyticsProxy.get().logButton("Restore backup");
        final String backupName = listView.getAdapter().getItem(position).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.want_erase_and_restore, backupName));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                restoreBackup(backupName);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void restoreBackup(final String backupName) {
        startProgress(getString(R.string.restoring_backup));
        backupController.restoreBackup(dbClient, backupName);
    }

    private void fetchBackups() {
        startProgress(getString(R.string.fetching_backups));
        backupController.fetchBackups(dbClient);
    }

    private void removeBackup(String backupName) {
        startProgress(getString(R.string.removing_backup));
        backupController.removeBackup(dbClient, backupName);
    }

    private void logout() {
        preferenceController.writeDropboxAccessToken(null);
        Auth.startOAuth2Authentication(BackupActivity.this, APP_KEY);
        btnBackupNow.setEnabled(false);
    }
}

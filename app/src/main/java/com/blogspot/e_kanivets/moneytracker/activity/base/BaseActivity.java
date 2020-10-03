package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.di.AppComponent;

import butterknife.ButterKnife;

/**
 * Base implementation of {@link androidx.appcompat.app.AppCompatActivity} to describe some common
 * methods.
 * Created on 1/26/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseActivity";

    private Toolbar toolbar;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (initData()) initViews();
        else finish();
    }

    @LayoutRes
    protected abstract int getContentViewId();

    protected AppComponent getAppComponent() {
        return MtApp.get().getAppComponent();
    }

    protected boolean initData() {
        return true;
    }

    protected void initViews() {
        ButterKnife.bind(BaseActivity.this);
        toolbar = initToolbar();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract Toolbar initToolbar();

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int messageId) {
        Toast.makeText(BaseActivity.this, messageId, Toast.LENGTH_SHORT).show();
    }

    public void startProgress(@Nullable String message) {
        ProgressDialog progressDialog = getProgressDialog();
        if (progressDialog == null) return;
        if (message != null) progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void stopProgress() {
        if (getProgressDialog() == null) return;
        getProgressDialog().dismiss();
    }

    public void showAlert(@Nullable String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) progressDialog = new ProgressDialog(this);
        return progressDialog;
    }
}

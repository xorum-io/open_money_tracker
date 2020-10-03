package com.blogspot.e_kanivets.moneytracker.activity.base;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;

/**
 * Base implementation of {@link AppCompatActivity} to describe some common
 * methods.
 * Created on 1/26/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseBackActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseBackActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        return toolbar;
    }
}

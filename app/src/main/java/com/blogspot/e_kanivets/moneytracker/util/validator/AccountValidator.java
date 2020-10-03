package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class for Account validation.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public class AccountValidator implements IValidator<Account> {

    @NonNull
    private final Context context;

    @BindView(R.id.til_title)
    TextInputLayout tilTitle;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.til_init_sum)
    TextInputLayout tilInitSum;
    @BindView(R.id.et_init_sum)
    EditText etInitSum;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;

    public AccountValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        String title = etTitle.getText().toString().trim();
        double initSum = Double.MAX_VALUE;

        try {
            initSum = Double.parseDouble(etInitSum.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean valid = true;

        if (title.isEmpty()) {
            tilTitle.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        if (initSum == Double.MAX_VALUE) {
            tilInitSum.setError(context.getString(R.string.field_cant_be_empty));
            initSum = 0;
            valid = false;
        }

        if (Math.abs(initSum) > MAX_ABS_VALUE) {
            tilInitSum.setError(context.getString(R.string.too_rich_or_poor));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etInitSum.addTextChangedListener(new ClearErrorTextWatcher(tilInitSum));
    }
}

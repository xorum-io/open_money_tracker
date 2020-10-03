package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class for Account validation.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public class RecordValidator implements IValidator<Record> {

    @NonNull
    private final Context context;

    @BindView(R.id.til_title)
    TextInputLayout tilTitle;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.til_category)
    TextInputLayout tilCategory;
    @BindView(R.id.etCategory)
    EditText etCategory;
    @BindView(R.id.til_price)
    TextInputLayout tilPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.spinnerAccount)
    AppCompatSpinner spinnerAccount;

    public RecordValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;

        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        String category = etCategory.getText().toString().trim();

        if (category.isEmpty()) {
            tilCategory.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        //Check if price is valid
        double price = Double.MAX_VALUE;
        try {
            price = Double.parseDouble(etPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (price == Double.MAX_VALUE) {
            tilPrice.setError(context.getString(R.string.field_cant_be_empty));
            price = 0;
            valid = false;
        }

        if (price > MAX_ABS_VALUE) {
            tilPrice.setError(context.getString(R.string.too_rich));
            valid = false;
        }

        if (!spinnerAccount.isEnabled()) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etPrice.addTextChangedListener(new ClearErrorTextWatcher(tilPrice));
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etCategory.addTextChangedListener(new ClearErrorTextWatcher(tilCategory));
    }
}

package com.blogspot.e_kanivets.moneytracker.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Custom TextWatcher which resets an error when text changed
 * Created on 07.12.2016.
 *
 * @author Evgenii Kanivets
 */

public class ClearErrorTextWatcher implements TextWatcher {
    @NonNull
    private TextInputLayout til;

    public ClearErrorTextWatcher(@NonNull TextInputLayout til) {
        this.til = til;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        til.setErrorEnabled(false);
        til.setError(null);
    }
}

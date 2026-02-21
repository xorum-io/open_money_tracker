package com.blogspot.e_kanivets.moneytracker.util.validator

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class ClearErrorTextWatcher(private val til: TextInputLayout) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        til.isErrorEnabled = false
        til.error = null
    }
}

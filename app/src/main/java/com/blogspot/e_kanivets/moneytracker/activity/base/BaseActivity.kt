package com.blogspot.e_kanivets.moneytracker.activity.base

import android.app.ProgressDialog
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
        private set
    protected var progressDialog: ProgressDialog? = null

    protected abstract fun initToolbar(): Toolbar?

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes messageId: Int) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
    }

    fun startProgress(message: String?) {
        val pd = getOrCreateProgressDialog() ?: return
        if (message != null) pd.setMessage(message)
        pd.show()
    }

    fun stopProgress() {
        getOrCreateProgressDialog()?.dismiss()
    }

    fun showAlert(title: String?, message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun getOrCreateProgressDialog(): ProgressDialog? {
        if (progressDialog == null) progressDialog = ProgressDialog(this)
        return progressDialog
    }
}

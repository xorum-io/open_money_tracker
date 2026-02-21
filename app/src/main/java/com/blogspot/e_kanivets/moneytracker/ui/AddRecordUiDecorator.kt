package com.blogspot.e_kanivets.moneytracker.ui

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.annotation.StyleRes
import androidx.appcompat.app.ActionBar
import android.view.WindowManager
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.record.AddRecordActivity
import com.blogspot.e_kanivets.moneytracker.entity.data.Record

class AddRecordUiDecorator(private val activity: Activity) {

    @StyleRes
    private var dialogTheme = -1

    private val redLightColor: Int
    private val redDarkColor: Int
    private val greenLightColor: Int
    private val greenDarkColor: Int

    init {
        val resources = activity.resources
        @Suppress("DEPRECATION")
        redLightColor = resources.getColor(R.color.red_light)
        @Suppress("DEPRECATION")
        redDarkColor = resources.getColor(R.color.red_dark)
        @Suppress("DEPRECATION")
        greenLightColor = resources.getColor(R.color.green_light)
        @Suppress("DEPRECATION")
        greenDarkColor = resources.getColor(R.color.green_dark)
    }

    @StyleRes
    fun getTheme(type: Int): Int {
        if (dialogTheme == -1) {
            when (type) {
                Record.TYPE_EXPENSE ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialogTheme = R.style.RedDialogTheme
                    }
                Record.TYPE_INCOME ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialogTheme = R.style.GreenDialogTheme
                    }
            }
        }
        return dialogTheme
    }

    fun decorateActionBar(actionBar: ActionBar?, mode: AddRecordActivity.Mode, type: Int) {
        if (actionBar == null) return

        when (type) {
            Record.TYPE_EXPENSE -> {
                if (mode == AddRecordActivity.Mode.MODE_ADD)
                    actionBar.setTitle(R.string.title_add_expense)
                else actionBar.setTitle(R.string.title_edit_expense)

                actionBar.setBackgroundDrawable(ColorDrawable(redLightColor))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = redDarkColor
                }
            }
            Record.TYPE_INCOME -> {
                if (mode == AddRecordActivity.Mode.MODE_ADD)
                    actionBar.setTitle(R.string.title_add_income)
                else actionBar.setTitle(R.string.title_edit_income)

                actionBar.setBackgroundDrawable(ColorDrawable(greenLightColor))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = greenDarkColor
                }
            }
        }
    }
}

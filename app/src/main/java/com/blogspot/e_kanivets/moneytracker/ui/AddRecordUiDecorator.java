package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.record.AddRecordActivity;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

/**
 * Util class to encapsulate toolbar customization for AddRecordActivity
 * Created on 9/2/16.
 *
 * @author Evgenii Kanivets
 */
public class AddRecordUiDecorator {
    private Activity activity;

    @StyleRes
    private int dialogTheme = -1;

    private int redLightColor;
    private int redDarkColor;
    private int greenLightColor;
    private int greenDarkColor;

    @SuppressWarnings("deprecation")
    public AddRecordUiDecorator(@NonNull Activity activity) {
        this.activity = activity;

        Resources resources = activity.getResources();
        redLightColor = resources.getColor(R.color.red_light);
        redDarkColor = resources.getColor(R.color.red_dark);
        greenLightColor = resources.getColor(R.color.green_light);
        greenDarkColor = resources.getColor(R.color.green_dark);
    }

    /**
     * @param type of record to handle, may be TYPE_EXPENSE or TYPE_INCOME
     * @return theme res id
     */
    @StyleRes
    public int getTheme(int type) {
        if (dialogTheme == -1) {
            switch (type) {
                case Record.TYPE_EXPENSE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialogTheme = R.style.RedDialogTheme;
                    }
                    break;

                case Record.TYPE_INCOME:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialogTheme = R.style.GreenDialogTheme;
                    }
                    break;

                default:
                    break;
            }
        }

        return dialogTheme;
    }

    /**
     * @param actionBar to decorate
     * @param mode      - MODE_ADD or MODE_EDIT
     * @param type      of record to handle, may be TYPE_EXPENSE or TYPE_INCOME
     */
    public void decorateActionBar(@Nullable ActionBar actionBar, AddRecordActivity.Mode mode, int type) {
        if (actionBar == null) return;

        switch (type) {
            case Record.TYPE_EXPENSE:
                if (mode == AddRecordActivity.Mode.MODE_ADD)
                    actionBar.setTitle(R.string.title_add_expense);
                else actionBar.setTitle(R.string.title_edit_expense);

                actionBar.setBackgroundDrawable(new ColorDrawable(redLightColor));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(redDarkColor);
                }
                break;

            case Record.TYPE_INCOME:
                if (mode == AddRecordActivity.Mode.MODE_ADD)
                    actionBar.setTitle(R.string.title_add_income);
                else actionBar.setTitle(R.string.title_edit_income);

                actionBar.setBackgroundDrawable(new ColorDrawable(greenLightColor));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(greenDarkColor);
                }
                break;

            default:
                break;
        }
    }
}

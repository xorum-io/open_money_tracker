package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import androidx.annotation.Nullable;


/**
 * In free software builds, this class does nothing as Crashlytics can't be used.
 * Created on 28/8/19.
 *
 * @author Fynn Godau
 */

public class CrashlyticsProxy {
    private static CrashlyticsProxy instance;

    public static CrashlyticsProxy get() {
        if (instance == null) {
            instance = new CrashlyticsProxy();
        }
        return instance;
    }

    private CrashlyticsProxy() {

    }

    public static void startCrashlytics(Context context) {

    }

    public void setEnabled(boolean enabled) {

    }

    public boolean isEnabled() {
        return false;
    }

    public boolean logEvent(@Nullable String eventName) {
        return false;
    }

    public boolean logButton(@Nullable String buttonName) {
        return false;
    }
}

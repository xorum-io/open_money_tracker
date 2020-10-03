package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Util class that wraps all Crashlytics interactions to disable Answers in
 * Debug mode and allow not including Crashlytics in free (fdroid) builds.
 * Created on 1/11/17.
 *
 * @author Evgenii Kanivets
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

    private boolean enabled;
    private static FirebaseAnalytics analytics;

    public static void startCrashlytics(Context context) {
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean logEvent(@Nullable String eventName) {
        if (enabled) {
            analytics.logEvent(eventName, null);
            return true;
        } else {
            return false;
        }
    }

    public boolean logButton(@Nullable String buttonName) {
        if (enabled) {
            analytics.logEvent(buttonName, null);
            return true;
        } else {
            return false;
        }
    }
}

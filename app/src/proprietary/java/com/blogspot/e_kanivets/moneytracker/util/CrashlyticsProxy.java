package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import androidx.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import io.fabric.sdk.android.Fabric;

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

    public static void startCrashlytics(Context context) {
        Fabric.with(context, new Crashlytics());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean logEvent(@Nullable String eventName) {
        if (enabled) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(eventName)
                    .putContentType("Event"));
            return true;
        } else {
            return false;
        }
    }

    public boolean logButton(@Nullable String buttonName) {
        if (enabled) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(buttonName)
                    .putContentType("Button"));
            return true;
        } else {
            return false;
        }
    }
}

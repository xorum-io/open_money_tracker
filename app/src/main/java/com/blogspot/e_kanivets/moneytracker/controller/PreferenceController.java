package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.R;

import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Controller class to encapsulate Shared Preferences handling logic.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class PreferenceController {
    private static final String APP_RATED = "app_rated";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String KEY_FIRST_TS = "key_first_ts";
    private static final String KEY_LAST_TS = "key_last_ts";
    private static final String KEY_PERIOD_TYPE = "key_period_type";
    private static final String KEY_DROPBOX_ACCESS_TOKEN = "key_dropbox_access_token";
    private static final String KEY_FILTERED_CATEGORIES = "key_filtered_categories";
    private static final String KEY_RECORD_TITLE_CATEGORY_PAIRS = "key_record_title_category_pairs";

    private static final int RATE_PERIOD = 5;

    @NonNull private Context context;

    public PreferenceController(@NonNull Context context) {
        this.context = context;
    }

    public void addLaunchCount() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(LAUNCH_COUNT, preferences.getInt(LAUNCH_COUNT, 0) + 1);
        editor.apply();
    }

    public boolean checkRateDialog() {
        SharedPreferences preferences = getDefaultPrefs();

        boolean appRated = preferences.getBoolean(APP_RATED, false);
        if (appRated) return false;

        int launchCount = preferences.getInt(LAUNCH_COUNT, 0);
        return launchCount % RATE_PERIOD == 0;
    }

    public void appRated() {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(APP_RATED, true);
        editor.apply();
    }

    public void writeFirstTs(long firstTs) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(KEY_FIRST_TS, firstTs);
        editor.apply();
    }

    public void writeLastTs(long lastTs) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(KEY_LAST_TS, lastTs);
        editor.apply();
    }

    public void writePeriodType(String periodType) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(KEY_PERIOD_TYPE, periodType);
        editor.apply();
    }

    public void writeDropboxAccessToken(String accessToken) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(KEY_DROPBOX_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public void writeFilteredCategories(@Nullable Set<String> categorySet) {
        SharedPreferences.Editor editor = getEditor();
        editor.putStringSet(KEY_FILTERED_CATEGORIES, categorySet);
        editor.apply();
    }

    public void writeRecordTitleCategoryPairs(@NonNull Map<String, String> map) {
        Set<String> set = new TreeSet<>();
        for (String key : map.keySet()) {
            set.add(key + ";" + map.get(key));
        }

        SharedPreferences.Editor editor = getEditor();
        editor.putStringSet(KEY_RECORD_TITLE_CATEGORY_PAIRS, set);
        editor.apply();
    }

    public long readDefaultAccountId() {
        String defaultAccountPref = context.getString(R.string.pref_default_account);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return Long.parseLong(preferences.getString(defaultAccountPref, "-1"));
    }

    @Nullable public String readDefaultCurrency() {
        String defaultCurrencyPref = context.getString(R.string.pref_default_currency);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(defaultCurrencyPref, null);
    }

    @NonNull public String readDisplayPrecision() {
        String displayPrecisionPref = context.getString(R.string.pref_display_precision);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(displayPrecisionPref, FormatController.PRECISION_MATH);
    }

    @Nullable public String readNonSubstitutionCurrency() {
        String nonSubstitutionCurrencyPref = context.getString(R.string.pref_non_substitution_currency);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(nonSubstitutionCurrencyPref, DbHelper.DEFAULT_ACCOUNT_CURRENCY);
    }

    public long readFirstTs() {
        return getDefaultPrefs().getLong(KEY_FIRST_TS, -1);
    }

    public long readLastTs() {
        return getDefaultPrefs().getLong(KEY_LAST_TS, -1);
    }

    @Nullable public String readPeriodType() {
        return getDefaultPrefs().getString(KEY_PERIOD_TYPE, null);
    }

    @Nullable public String readDropboxAccessToken() {
        return getDefaultPrefs().getString(KEY_DROPBOX_ACCESS_TOKEN, null);
    }

    @NonNull public Set<String> readFilteredCategories() {
        // http://stackoverflow.com/questions/14034803/misbehavior-when-trying-to-store-a-string-set-using-sharedpreferences/14034804#14034804
        return new HashSet<>(getDefaultPrefs().getStringSet(KEY_FILTERED_CATEGORIES, new HashSet<String>()));
    }

    @NonNull public Map<String, String> readRecordTitleCategoryPairs() {
        Map<String, String> map = new TreeMap<>();

        Set<String> set = getDefaultPrefs().getStringSet(KEY_RECORD_TITLE_CATEGORY_PAIRS, new HashSet<String>());
        for (String entry : set) {
            String[] words = entry.split(";");
            if (words.length == 2) {
                map.put(words[0], words[1]);
            }
        }

        return map;
    }

    @NonNull private SharedPreferences getDefaultPrefs() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @NonNull private SharedPreferences.Editor getEditor() {
        return getDefaultPrefs().edit();
    }
}

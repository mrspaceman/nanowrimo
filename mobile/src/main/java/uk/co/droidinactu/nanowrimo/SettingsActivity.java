package uk.co.droidinactu.nanowrimo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_NANO_UPDATE_AUTO = "pref_nano_auto_update";
    public static final String KEY_PREF_NANO_USERNAME = "pref_nano_username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_NANO_UPDATE_AUTO)) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            if (sharedPreferences.getBoolean(key, false)) {
                connectionPref.setSummary(getResources().getString(R.string.pref_nano_auto_update_true));
            } else {
                connectionPref.setSummary(getResources().getString(R.string.pref_nano_auto_update_false));
            }
        }
    }
}

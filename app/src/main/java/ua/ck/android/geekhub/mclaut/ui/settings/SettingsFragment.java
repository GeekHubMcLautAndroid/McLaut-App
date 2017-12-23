package ua.ck.android.geekhub.mclaut.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import ua.ck.android.geekhub.mclaut.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}

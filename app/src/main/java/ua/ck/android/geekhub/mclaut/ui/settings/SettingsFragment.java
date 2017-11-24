package ua.ck.android.geekhub.mclaut.ui.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.settings.SettingsViewModel;

/**
 * Created by Sergo on 11/24/17.
 */

public class SettingsFragment extends Fragment {

    private SettingsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        return inflater.inflate(R.layout.fragment_settings,container,false);
    }
}

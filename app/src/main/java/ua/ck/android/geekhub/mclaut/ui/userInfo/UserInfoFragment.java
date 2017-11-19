package ua.ck.android.geekhub.mclaut.ui.userInfo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.ck.android.geekhub.mclaut.R;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);

        return inflater.inflate(R.layout.fragment_user_info,container,false);
    }
}

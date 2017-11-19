package ua.ck.android.geekhub.mclaut.ui.withdrawalsInfo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.ck.android.geekhub.mclaut.R;


public class WithdrawalsInfoFragment extends Fragment {

    private WithdrawalsInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(WithdrawalsInfoViewModel.class);

        return inflater.inflate(R.layout.fragment_withdrawals_info,container,false);
    }
}

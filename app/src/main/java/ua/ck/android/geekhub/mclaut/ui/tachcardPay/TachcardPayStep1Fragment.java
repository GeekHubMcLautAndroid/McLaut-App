package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayStep1Fragment extends Fragment {
    private TachcardPayViewModel viewModel;

    public interface OnPaymentRedirect{
        public void redirect(String location,String html);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(TachcardPayViewModel.class);
        return inflater.inflate(R.layout.fragment_tachcard_pay, container, false);
    }
}

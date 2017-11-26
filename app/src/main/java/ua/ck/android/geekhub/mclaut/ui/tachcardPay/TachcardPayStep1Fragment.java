package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.authorization.LoginViewModel;

public class TachcardPayStep1Fragment extends Fragment {
    @BindView(R.id.fragment_tachcard_pay_summ_text_input_edit_text)
    TextInputEditText summTIEL;
    @BindView(R.id.fragment_tachcard_pay_summ_text_input_layout)
    TextInputLayout summTIL;
    @BindView(R.id.fragment_tachcard_pay_card_number_text_input_edit_text)
    TextInputEditText cardNumberTIET;
    @BindView(R.id.fragment_tachcard_pay_card_number_text_input_layout)
    TextInputLayout cardNumberTIL;
    @BindView(R.id.fragment_tachcard_pay_cvv_text_input_edit_text)
    TextInputEditText cvvTIEL;
    @BindView(R.id.fragment_tachcard_pay_cvv_text_input_layout)
    TextInputLayout cvvTIL;
    @BindView(R.id.fragment_tachcard_pay_mm_text_input_edit_text)
    TextInputEditText mmTIEL;
    @BindView(R.id.fragment_tachcard_pay_mm_text_input_layout)
    TextInputLayout mmTIL;
    @BindView(R.id.fragment_tachcard_pay_yy_text_input_edit_text)
    TextInputEditText yyTIEL;
    @BindView(R.id.fragment_tachcard_pay_yy_text_input_layout)
    TextInputLayout yyTIL;
    @BindView(R.id.fragment_tachcard_pay_select_card_image_button)
    ImageButton selectCardButton;
    @BindView(R.id.tachcard_pay_confirm)
    CircularProgressButton confirmCPB;
    private TachcardPayViewModel viewModel;


    OnPaymentRedirect paymentRedirectListener;


    public interface OnPaymentRedirect {
        public void redirect(String location, String html);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(TachcardPayViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_tachcard_pay, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(this).get(TachcardPayViewModel.class);
        viewModel.getProgressStatusData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
        return rootView;
    }

    @OnClick(R.id.tachcard_pay_confirm)
    public void confirmPayment() {
        String summ = summTIEL.getText().toString();
        String cardNumber = cardNumberTIET.getText().toString();
        String mm = mmTIEL.getText().toString();
        String yy = yyTIEL.getText().toString();
        String cvv = cvvTIEL.getText().toString();
        int[] digits = new int[cardNumber.length()];
        if (Double.parseDouble(summ) < 5) {
            summTIL.setError("Мінімальна сума поповнення 5 грн.");
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
        }
        if (cardNumber.length() < 16 || checkLuhn(digits)) {
            cardNumberTIL.setError("Введіть коректний номер картки.");
            hideProgress();
            return;
        }
        if (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12 || mm.length() != 2) {
            mmTIL.setErrorEnabled(true);
            return;
        }
        if (cvv.length() != 3) {
            cvvTIL.setErrorEnabled(true);
            return;
        }
    }

    public void showProgress() {
        confirmCPB.startAnimation();
        selectCardButton.setEnabled(false);
        cardNumberTIET.setEnabled(false);
        mmTIEL.setEnabled(false);
        yyTIEL.setEnabled(false);
        cvvTIEL.setEnabled(false);
    }

    public void hideProgress() {
        confirmCPB.revertAnimation();
        selectCardButton.setEnabled(true);
        cardNumberTIET.setEnabled(true);
        mmTIEL.setEnabled(true);
        yyTIEL.setEnabled(true);
        cvvTIEL.setEnabled(true);
    }

    private boolean checkLuhn(int[] digits) {
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {
            int digit = digits[length - i - 1];
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }
}

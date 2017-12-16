package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.ck.android.geekhub.mclaut.R;

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
    @BindView(R.id.fragment_tachcard_pay_save_card_checkbox)
    CheckBox saveCardCB;
    @BindView(R.id.fragment_tachcard_pay_save_card_text)
    TextView saveCardT;
    @BindView(R.id.tachcard_pay_confirm)
    CircularProgressButton confirmCPB;
    private TachcardPayViewModel viewModel;

    public interface OnPaymentRedirect {
        void redirect(String location, String html);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(TachcardPayViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_tachcard_pay, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(this).get(TachcardPayViewModel.class);
        viewModel.getProgressStatusData().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgress();
            } else {
                hideProgress();
            }
        });
        viewModel.getSetError().observe(this, integer -> {
            summTIL.setErrorEnabled(false);
            cardNumberTIL.setErrorEnabled(false);
            mmTIL.setErrorEnabled(false);
            yyTIL.setErrorEnabled(false);
            cvvTIL.setErrorEnabled(false);
            switch (integer) {
                case 0:
                    summTIL.setError(getString(R.string.error_minimum_refung));
                    summTIL.setErrorEnabled(true);
                    break;
                case 1:
                    cardNumberTIL.setError(getString(R.string.error_card_number));
                    cardNumberTIL.setErrorEnabled(true);
                    break;
                case 2:
                    mmTIL.setErrorEnabled(true);
                    yyTIL.setErrorEnabled(true);
                    break;
                case 3:
                    cvvTIL.setErrorEnabled(true);
                    break;
                default:
                    break;
            }
        });
        viewModel.getRedirectDocument().observe(this, document -> {
            OnPaymentRedirect paymentRedirectListener = (OnPaymentRedirect) getActivity();
            paymentRedirectListener.redirect(document.location(), document.html());
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
        viewModel.pay(getContext(), summ, cardNumber, mm, yy, cvv);
    }

    @OnClick(R.id.fragment_tachcard_pay_save_card_text)
    public void changeCheckBox() {
        saveCardCB.setChecked(!saveCardCB.isChecked());
    }

    public void showProgress() {
        confirmCPB.startAnimation();
        summTIEL.setEnabled(false);
        selectCardButton.setEnabled(false);
        cardNumberTIET.setEnabled(false);
        mmTIEL.setEnabled(false);
        yyTIEL.setEnabled(false);
        cvvTIEL.setEnabled(false);
    }

    public void hideProgress() {
        confirmCPB.revertAnimation();
        summTIEL.setEnabled(true);
        selectCardButton.setEnabled(true);
        cardNumberTIET.setEnabled(true);
        mmTIEL.setEnabled(true);
        yyTIEL.setEnabled(true);
        cvvTIEL.setEnabled(true);
    }
}

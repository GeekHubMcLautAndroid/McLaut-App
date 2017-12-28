package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.model.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.ui.settings.SettingsActivity;

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
    @BindView(R.id.fragment_tachcard_pay_result_summ_text)
    TextView resultSummTV;

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
            if (document != null) {
                OnPaymentRedirect paymentRedirectListener = (OnPaymentRedirect) getActivity();
                paymentRedirectListener.redirect(document.location(), document.html());
            } else {
                hideProgress();
                Toast.makeText(getActivity(), getString(R.string.payment_network_error), Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    @OnTextChanged(R.id.fragment_tachcard_pay_summ_text_input_edit_text)
    public void showFinalSumm() {
        String summString = summTIEL.getText().toString();
        if (summString.length() < 1) {
            resultSummTV.setText(R.string.payment_amount_due_empty);
        } else {
            double summDouble = Double.parseDouble(summString);
            if (summDouble < 5) {
                resultSummTV.setText(R.string.payment_amount_due_empty);
                return;
            }
            double tax = summDouble * 0.025;
            if (tax > 1) {
                double result = summDouble + tax;
                String outStr = getString(R.string.payment_amount_due) + String.format(Locale.ENGLISH, "%.2f", result) + getString(R.string.uah_symbol);
                resultSummTV.setText(outStr);
            } else {
                double result = summDouble + 1.;
                String outStr = getString(R.string.payment_amount_due) + String.format(Locale.ENGLISH, "%.2f", result) + getString(R.string.uah_symbol);
                resultSummTV.setText(outStr);
            }
        }
    }

    @OnClick(R.id.fragment_tachcard_pay_select_card_image_button)
    public void openSelectCardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        HashMap<String, CardInfoEntity> mapCardEntities = viewModel.getCards();
        String[] type = new String[]{};
        String[] keys = mapCardEntities.keySet().toArray(type);
        builder.setTitle(R.string.payment_select_card).setItems(keys, (dialogInterface, i) -> {
            cardNumberTIET.setText(mapCardEntities.get(keys[i]).getCardNumber());
            mmTIEL.setText(mapCardEntities.get(keys[i]).getEndMonth());
            yyTIEL.setText(mapCardEntities.get(keys[i]).getEndYear());
            mapCardEntities.get(keys[i]).incrementCounterOfUses(getContext());
        }).setPositiveButton(getString(R.string.payment_goto_card_settings), (dialogInterface, i) -> {
            Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intentSettings);
        }).setNegativeButton(getString(R.string.dialog_button_cancel), (dialogInterface, i) -> {
        });
        builder.create().show();
    }

    @OnClick(R.id.tachcard_pay_confirm)
    public void confirmPayment() {
        String summ = summTIEL.getText().toString();
        String cardNumber = cardNumberTIET.getText().toString();
        String mm = mmTIEL.getText().toString();
        String yy = yyTIEL.getText().toString();
        String cvv = cvvTIEL.getText().toString();
        summTIL.setErrorEnabled(false);
        cardNumberTIL.setErrorEnabled(false);
        mmTIL.setErrorEnabled(false);
        yyTIL.setErrorEnabled(false);
        cvvTIL.setErrorEnabled(false);
        viewModel.pay(getContext(), summ, cardNumber, mm, yy, cvv, saveCardCB.isChecked());
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

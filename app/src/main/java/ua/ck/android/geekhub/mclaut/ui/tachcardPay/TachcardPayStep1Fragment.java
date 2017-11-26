package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


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

public class TachcardPayStep1Fragment extends Fragment {
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
        return rootView;
    }

    @OnClick(R.id.tachcard_pay_confirm)
    public void confirmPayment() {

    }

    class Payment extends AsyncTask<String, Void, Integer> {
        Document page;

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 1:
                    paymentRedirectListener.redirect(page.location(), page.html());
                    break;
                default:
                    break;
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Connection.Response main;
            Element secondStage;
            String tc_user_session;
            try {
                main = Jsoup.connect(strings[0]).userAgent("Mozilla").execute();
                secondStage = main.parse().getElementById("secondStage");
                tc_user_session = main.cookie("tc_user_session");
            } catch (IOException e) {
                return null;
            }
            Elements hiddenInputs = secondStage.select("input[type=hidden]");
            String pan = strings[1];
            String mm = strings[2];
            String yy = strings[3];
            String cvv = strings[4];
            try {
                page = Jsoup.connect("https://user.tachcard.com/uk/pay-from-card")
                        .data(hiddenInputs.get(0).attr("name"), hiddenInputs.get(0).attr("value"))
                        .data(hiddenInputs.get(1).attr("name"), "1")
                        .data(hiddenInputs.get(2).attr("name"), hiddenInputs.get(2).attr("value"))
                        .data("pan", pan)
                        .data("mm", mm)
                        .data("yy", yy)
                        .data("cvv", cvv)
                        .cookie("tc_user_session", tc_user_session)
                        .userAgent("Mozilla")
                        .ignoreHttpErrors(true)
                        .followRedirects(true)
                        .post();
            } catch (IOException e) {
                return null;
            }
            return 1;
        }
    }
}

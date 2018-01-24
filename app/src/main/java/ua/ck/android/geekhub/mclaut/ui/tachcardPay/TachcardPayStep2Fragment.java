package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayStep2Fragment extends Fragment {

    WebView outWebView;
    String location;
    String html;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle argument = getArguments();
        location = argument.getString("location");
        html = argument.getString("html");
        outWebView = new WebView(inflater.getContext());
        outWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.equals(location) && !url.contains("user.tachcard.com")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.payment_end), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    return;
                }
                super.onPageFinished(view, url);
            }
        });
        outWebView.getSettings().setJavaScriptEnabled(true);
        return outWebView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outWebView.saveState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            String mine = "text/html";
            String encoding = "UTF-8";
            outWebView.loadDataWithBaseURL(location, html, mine, encoding, null);
        } else {
            outWebView.restoreState(savedInstanceState);
        }
    }
}

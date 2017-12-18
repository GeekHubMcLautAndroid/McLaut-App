package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class TachcardPayStep2Fragment extends Fragment {

    String location;
    String html;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle argument = getArguments();
        location = argument.getString("location");
        html = argument.getString("html");
        WebView outWebView = new WebView(inflater.getContext());
        outWebView.getSettings().setJavaScriptEnabled(true);
        String mine = "text/html";
        String encoding = "UTF-8";
        outWebView.loadDataWithBaseURL(location, html, mine, encoding, null);
        return outWebView;
    }
}

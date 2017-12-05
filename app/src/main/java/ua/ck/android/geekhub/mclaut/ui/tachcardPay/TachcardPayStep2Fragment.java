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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = savedInstanceState.getString("location");
        html = savedInstanceState.getString("html");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        WebView outWebView = new WebView(inflater.getContext());
        outWebView.getSettings().setJavaScriptEnabled(true);
        String mine = "text/html";
        String encoding = "UTF-8";
        outWebView.loadDataWithBaseURL(location, html, mine, encoding, null);
        return outWebView;
    }
}

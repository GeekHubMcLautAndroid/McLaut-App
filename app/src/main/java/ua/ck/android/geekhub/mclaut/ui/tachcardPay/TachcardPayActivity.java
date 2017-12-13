package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import org.jsoup.nodes.Document;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayActivity extends AppCompatActivity{
    @BindView(R.id.flPaymentContent)
    FrameLayout mFrameLayout;
    private FragmentManager fragmentManager;
    private static TachcardPayActivity instance;

    public static TachcardPayActivity getInstance() {
        if (instance == null) {
            instance = new TachcardPayActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tachcard_pay);
        ButterKnife.bind(this);
        Fragment fragment = null;
        try {
            fragment = TachcardPayStep1Fragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();
    }

    public void redirect(String location, String html) {
        Fragment fragment = null;
        try {
            fragment = TachcardPayStep2Fragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle arg = new Bundle();
        arg.putString("location", location);
        arg.putString("html", html);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();
    }
}

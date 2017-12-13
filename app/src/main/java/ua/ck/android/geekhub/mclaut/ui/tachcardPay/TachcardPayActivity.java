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
import android.widget.Toast;

import org.jsoup.nodes.Document;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayActivity extends AppCompatActivity implements TachcardPayStep1Fragment.OnPaymentRedirect {
    @BindView(R.id.flPaymentContent)
    FrameLayout mFrameLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tachcard_pay);
        ButterKnife.bind(this);
        Fragment fragment = new TachcardPayStep1Fragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();

    }

    @Override
    public void redirect(String location, String html) {
        Fragment fragment = new TachcardPayStep2Fragment();
        Bundle arg = new Bundle();
        arg.putString("location", location);
        arg.putString("html", html);
        fragment.setArguments(arg);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();
    }
}

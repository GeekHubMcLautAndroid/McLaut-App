package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayActivity extends AppCompatActivity implements TachcardPayStep1Fragment.OnPaymentRedirect {
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tachcard_pay);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.payment);
        if (savedInstanceState == null) {
            fragment = new TachcardPayStep1Fragment();
        } else {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "Fragment");

        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();

    }

    @Override
    public void redirect(String location, String html) {
        fragment = new TachcardPayStep2Fragment();
        Bundle arg = new Bundle();
        arg.putString("location", location);
        arg.putString("html", html);
        fragment.setArguments(arg);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flPaymentContent, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "Fragment", fragment);
    }
}

package ua.ck.android.geekhub.mclaut.ui.tachcardPay;


import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ua.ck.android.geekhub.mclaut.R;

public class TachcardPayActivity extends AppCompatActivity implements TachcardPayStep1Fragment.OnPaymentRedirect {
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tachcard_pay);
        //noinspection ConstantConditions
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tachcard_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tachcard_activity_menu_reload:
                AnimatedVectorDrawable drawableIcon = (AnimatedVectorDrawable) item.getIcon();
                drawableIcon.stop();
                drawableIcon.start();
                fragment = new TachcardPayStep1Fragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flPaymentContent, fragment)
                        .commit();
                break;
            case R.id.tachcard_activity_menu_open_in_browser:
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tachcard_pay_url)));
                startActivity(myIntent);
                break;
            case android.R.id.home:
                finish();
        }
        return true;
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

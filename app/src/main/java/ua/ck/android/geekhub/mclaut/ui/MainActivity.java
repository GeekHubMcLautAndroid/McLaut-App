package ua.ck.android.geekhub.mclaut.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.settings.SettingsFragment;
import ua.ck.android.geekhub.mclaut.ui.userInfo.UserInfoFragment;
import ua.ck.android.geekhub.mclaut.ui.withdrawalsInfo.WithdrawalsInfoFragment;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.drawer_navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private int idActiveFragment = R.id.general_information_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupDrawerNavigationView(mNavigationView);
        setSupportActionBar(toolbar);

        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setToolbarTitle();

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private final String _idDrawerItem = "fragmentId";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(_idDrawerItem, idActiveFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idActiveFragment = savedInstanceState.getInt(_idDrawerItem);
        selectDrawerItem();
    }

    private void setupDrawerNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        idActiveFragment = item.getItemId();
                        selectDrawerItem();
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem() {
        Class fragmentClass;
        switch (idActiveFragment) {
            case R.id.cash_operations:
                fragmentClass = WithdrawalsInfoFragment.class;
                break;
            case R.id.general_information_fragment:
                fragmentClass = UserInfoFragment.class;
                break;
            case R.id.settings_fragment:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = UserInfoFragment.class;
        }
        intializedFragment(fragmentClass);
    }


    private void intializedFragment(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e){
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();

        setToolbarTitle();
        mDrawerLayout.closeDrawers();
    }

    private void setToolbarTitle() {
        Menu menu = mNavigationView.getMenu();
        this.setTitle(
                menu.findItem(idActiveFragment)
                        .getTitle());
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return  new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_toggle_open,
                R.string.drawer_toggle_close);
    }
}

package ua.ck.android.geekhub.mclaut.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.paymentsInfo.PaymentsInfoFragment;
import ua.ck.android.geekhub.mclaut.ui.userInfo.UserInfoFragment;
import ua.ck.android.geekhub.mclaut.ui.withdrawalsInfo.WithdrawalsInfoFragment;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.drawer_navigation_view)
    NavigationView mNavigationView;

    private FragmentManager fragmentManager;
    private MainViewModel mainViewModel;
    private Boolean isFirstInit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(isFirstInit){
            isFirstInit = false;
            firstInit();
        }

        fragmentManager = getSupportFragmentManager();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getSelectedItem().observe(this, new Observer<MenuItem>() {

            @Override
            public void onChanged(@Nullable MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.general_information_fragment:
                        selectDrawerItem(menuItem);
                    case R.id.payments_fragment :
                        selectDrawerItem(menuItem);
                        break;
                    case R.id.withdrawals_fragment :
                        selectDrawerItem(menuItem);
                        break;
                    case R.id.settings_fragment :
                        selectDrawerItem(menuItem);
                        break;
                    default:
                        selectDrawerItem((MenuItem)findViewById(R.id.general_information_fragment));
                }

            }
        });
        setSupportActionBar(toolbar);
        setupDrawerNavigationView(mNavigationView);

    }

    private void setupDrawerNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mainViewModel.selectFragment(item);
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.payments_fragment:
                fragmentClass = PaymentsInfoFragment.class;
                break;
            case R.id.withdrawals_fragment:
                fragmentClass = WithdrawalsInfoFragment.class;
                break;
            case R.id.general_information_fragment:
                fragmentClass = UserInfoFragment.class;
                break;
            default:
                fragmentClass = UserInfoFragment.class;
        }
        intializedFragment(fragmentClass, item);
    }

    private void intializedFragment(Class fragmentClass, MenuItem item){
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

        item.setChecked(true);
        setTitle(item.getTitle());

        mDrawerLayout.closeDrawers();
    }

    private void firstInit(){
        Fragment fragment = null;
        try {
            fragment = UserInfoFragment.class.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();

        setTitle(R.string.general_information);
    }
}

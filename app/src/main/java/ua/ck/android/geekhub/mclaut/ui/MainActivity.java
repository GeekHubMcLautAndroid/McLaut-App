package ua.ck.android.geekhub.mclaut.ui;

import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.tools.McLautAppExecutor;
import ua.ck.android.geekhub.mclaut.ui.authorization.LoginActivity;
import ua.ck.android.geekhub.mclaut.ui.cashTransactions.CashTransactionsFragment;
import ua.ck.android.geekhub.mclaut.ui.settings.SettingsFragment;
import ua.ck.android.geekhub.mclaut.ui.tachcardPay.TachcardPayActivity;
import ua.ck.android.geekhub.mclaut.ui.userInfo.UserInfoFragment;

public class MainActivity extends AppCompatActivity implements Observer<Pair<String,String>> {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.drawer_navigation_view)
    NavigationView mNavigationView;

    private TextView drawerUserId;
    private TextView drawerUserName;
    private ImageButton buttonSelectLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private int idActiveFragment = R.id.general_information_fragment;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        drawerUserId = mNavigationView.getHeaderView(0)
                .findViewById(R.id.tv_header_user_id);
        drawerUserName = mNavigationView.getHeaderView(0)
                .findViewById(R.id.tv_header_user_name);
        buttonSelectLayout = mNavigationView.getHeaderView(0)
                .findViewById(R.id.bt_select_user);

        setupDrawerNavigationView(mNavigationView);
        setSupportActionBar(toolbar);

        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getUserIdAndName().observe(this,this);

        setToolbarTitle();

        selectDrawerItem();

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

                        if(item.getActionView() != null){
                            ImageButton button = item.getActionView()
                                    .findViewById(R.id.bt_remove_user);
                                McLautApplication.selectUser(button.getTag(R.string.btn_tag_key_id).toString());
                                mDrawerLayout.closeDrawers();
                        }
                        idActiveFragment = item.getItemId();
                        selectDrawerItem();
                        return true;
                    }
                }
        );
    }

    public void drawerLayoutButtonClick(View view){

        if(view.getTag().equals(getResources()
                .getString(R.string.enable_user_list))){
            selectUserListMenu();
        } else if (view.getTag().equals(getResources()
                .getString(R.string.enable_info))) {
            selectInfoMenu();
        } else if (view.getId() == R.id.bt_remove_user){

            DialogFragment dialogFragment = new RemoveUserDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.btn_tag_key_item_index),
                    (int) view.getTag(R.string.btn_tag_key_item_index));
            bundle.putString(getResources().getString(R.string.btn_tag_key_id),
                    (String) view.getTag(R.string.btn_tag_key_id));

            dialogFragment.setArguments(bundle);
            dialogFragment.show(getFragmentManager(), "millis");
        }
    }

    private void selectInfoMenu() {
        buttonSelectLayout.setTag(getResources()
                .getString(R.string.enable_user_list));
        buttonSelectLayout.setImageDrawable(getDrawable(R.drawable.account));
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.drawer_info_menu);
    }

    private void selectUserListMenu() {
        McLautAppExecutor.getInstance().mainThread().execute(()->{
            buttonSelectLayout.setTag(getResources()
                .getString(R.string.enable_info));
            buttonSelectLayout.setImageDrawable(getDrawable(R.drawable.info));
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_users_menu);

            TextView tvName;
            ImageButton btRemove;

            int index = 0;

            for (HashMap.Entry<String, UserCharacteristic> entry :
                    Repository.getInstance().getMapUsersCharacteristic().getValue().entrySet()){

                String name = entry.getValue().getInfo().getName();
                String id = entry.getValue().getInfo().getId();

                mNavigationView.getMenu().add(
                        R.id.users_list,
                        R.id.users_list + index,
                        Menu.NONE,
                        null
                ).setActionView(R.layout.custom_item);

                tvName = mNavigationView.getMenu().getItem(index)
                        .getActionView().findViewById(R.id.tv_name_custom_item);
                btRemove = mNavigationView.getMenu().getItem(index)
                        .getActionView().findViewById(R.id.bt_remove_user);

                tvName.setText(name);
                btRemove.setTag(R.string.remove_user);
                btRemove.setTag(R.string.btn_tag_key_id, id);
                btRemove.setTag(R.string.btn_tag_key_item_index, index);
                index++;
            }
        });

    }

    private void selectDrawerItem() {
        Class fragmentClass;
        switch (idActiveFragment) {
            case R.id.cash_operations:
                fragmentClass = CashTransactionsFragment.class;
                break;
            case R.id.general_information_fragment:
                fragmentClass = UserInfoFragment.class;

                break;
            case R.id.settings_fragment:
                fragmentClass = SettingsFragment.class;

                break;
            case R.id.payment_activity:
                Intent intentTachCar = new Intent(getBaseContext(), TachcardPayActivity.class);
                startActivity(intentTachCar);
                return;
            case R.id.add_new_user:
                Intent intentLoginActivity = new Intent(getBaseContext(), LoginActivity.class);
                intentLoginActivity.putExtra(LoginActivity.ADD_NEW_USER, true);
                startActivity(intentLoginActivity);
                return;
            default:
                fragmentClass = UserInfoFragment.class;
                idActiveFragment = R.id.general_information_fragment;

                break;
        }
        intializedFragment(fragmentClass);
    }


    private void intializedFragment(Class fragmentClass){
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .commit();

            setToolbarTitle();
            mDrawerLayout.closeDrawers();

        } catch (Exception e){
            e.printStackTrace();
        }

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
                R.string.drawer_toggle_close){
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(!buttonSelectLayout.getTag().equals(getResources()
                        .getString(R.string.enable_user_list))) {
                    selectInfoMenu();
                }
            }
        };
    }

    //ViewModel LiveData observer
    @Override
    public void onChanged(@Nullable Pair<String, String> stringStringHashMap) {
        if (stringStringHashMap != null) {
            drawerUserId.setText(stringStringHashMap.first);
            drawerUserName.setText(stringStringHashMap.second);
        }
    }
}

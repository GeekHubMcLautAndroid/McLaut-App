package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class CashTransactionsViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> viewPagerFragmentList = new ArrayList<>();
    private List<String> viewPagerTitlesList = new ArrayList<>();

    public CashTransactionsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        viewPagerFragmentList.add(fragment);
        viewPagerTitlesList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        if (viewPagerFragmentList.size() > position) {
            return viewPagerFragmentList.get(position);
        }
        return null;
    }


    @Override
    public int getCount() {
        return viewPagerFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewPagerTitlesList.get(position);
    }
}

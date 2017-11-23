package ua.ck.android.geekhub.mclaut.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.MenuItem;
/**
 * Created by Sergo on 11/23/17.
 */

public class MainViewModel extends ViewModel {
    private MutableLiveData<MenuItem>  selectedFragment = new MutableLiveData<>();

    public void selectFragment(MenuItem item){
        selectedFragment.setValue(item);
    }

    public MutableLiveData<MenuItem> getSelectedItem(){
        return selectedFragment;
    }
}

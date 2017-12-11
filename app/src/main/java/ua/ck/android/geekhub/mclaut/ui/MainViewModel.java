package ua.ck.android.geekhub.mclaut.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashMap;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;

public class MainViewModel extends ViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private MutableLiveData<HashMap<String,String>> userIdAndName = new MutableLiveData<>();

    public MainViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(new Observer<HashMap<String, UserCharacteristic>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
                if ((stringUserCharacteristicHashMap != null)
                        &&(stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser()) != null)) {
                    postCurrentUserLiveData(stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser()));
                }
            }
        });
        McLautApplication.getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public MutableLiveData<HashMap<String, String>> getUserIdAndName() {
        return userIdAndName;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sharedPreferences.equals(McLautApplication.getPreferences())
                && key.equals(McLautApplication.getSelectedUser())) {
            HashMap<String, UserCharacteristic> userCharacteristicMap =
                    Repository.getInstance().getMapUsersCharacteristic().getValue();

            if ((userCharacteristicMap != null)
                    &&(userCharacteristicMap.get(McLautApplication.getSelectedUser()) != null)) {
               postCurrentUserLiveData(userCharacteristicMap.get(McLautApplication.getSelectedUser()));
            }
        }
    }
    private void postCurrentUserLiveData(UserCharacteristic currUser){
        HashMap<String,String> res = new HashMap<>();
        res.put("id", McLautApplication.getSelectedUser());
        res.put("name",currUser.
                getInfo().
                getName());
        userIdAndName.postValue(res);
    }
}

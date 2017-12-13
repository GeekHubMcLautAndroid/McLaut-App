package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;

public class TachcardPayViewModel extends ViewModel implements Observer<HashMap<String, UserCharacteristic>> {

    private UserInfoEntity userData;

    public TachcardPayViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);
    }

    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        UserCharacteristic characteristic = stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser());
        userData = characteristic.getInfo();
    }

    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> setError = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    public MutableLiveData<Boolean> getProgressStatusData() {
        return showProgressStatus;
    }

    public MutableLiveData<Integer> getSetError() {
        return setError;
    }

    public Document pay(Context context, String summ, String cardNumber, String mm, String yy, String cvv) {
        showProgressStatus.postValue(true);
        int[] digits = new int[cardNumber.length()];
        if (Double.parseDouble(summ) < 5) {
            setError.postValue(0);
            showProgressStatus.postValue(false);
            return null;
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
        }
        if (cardNumber.length() < 16 || !checkLuhn(digits)) {
            setError.postValue(1);
            showProgressStatus.postValue(false);
            return null;
        }
        if (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12 || mm.length() != 2) {
            setError.postValue(2);
            showProgressStatus.postValue(false);
            return null;
        }
        if (cvv.length() != 3) {
            setError.postValue(3);
            showProgressStatus.postValue(false);
            return null;
        }
        String[] regions = new String[]{"cherkasy", "smela", "kanev", "zoloto", "ph", "vatutino", "zven"};
        String baseUrl = context.getString(R.string.payment_baseUrl);
        baseUrl += regions[userData.getCity()];
        baseUrl += "?&amount=" + summ + "&account=" + userData.getAccount();
        return repo.getPaymentRedirection(baseUrl, cardNumber, mm, yy, cvv).getValue();
    }

    private boolean checkLuhn(int[] digits) {
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {
            int digit = digits[length - i - 1];
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }
}

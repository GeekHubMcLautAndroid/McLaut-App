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
import ua.ck.android.geekhub.mclaut.data.network.TachcardDataSource;

public class TachcardPayViewModel extends ViewModel implements Observer<HashMap<String, UserCharacteristic>> {
    private static TachcardPayViewModel instance;
    private UserInfoEntity userData;
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> setError = new MutableLiveData<>();
    private MutableLiveData<Document> redirectDocument = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();
    int errorMinimumRefungCode = 0;
    int errorCardNumberCode = 1;
    int errorMM_YYCode = 2;
    int errorCVVCode = 3;

    public static TachcardPayViewModel getInstance() {
        if (instance == null) {
            instance = new TachcardPayViewModel();
        }
        return instance;
    }

    MutableLiveData<Boolean> getProgressStatusData() {
        return showProgressStatus;
    }

    MutableLiveData<Integer> getSetError() {
        return setError;
    }

    public MutableLiveData<Document> getRedirectDocument() {
        return redirectDocument;
    }

    public TachcardPayViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);
    }

    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        UserCharacteristic characteristic = stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser());
        userData = characteristic.getInfo();
    }

    void pay(Context context, String summ, String cardNumber, String mm, String yy, String cvv) {
        showProgressStatus.postValue(true);
        int[] digits = new int[cardNumber.length()];
        if (Double.parseDouble(summ) < 5) {
            setError.postValue(errorMinimumRefungCode);
            showProgressStatus.postValue(false);
            return;
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
        }
        if (cardNumber.length() < 16 || !checkLuhn(digits)) {
            setError.postValue(errorCardNumberCode);
            showProgressStatus.postValue(false);
            return;
        }
        if (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12 || mm.length() != 2) {
            setError.postValue(errorMM_YYCode);
            showProgressStatus.postValue(false);
            return;
        }
        if (cvv.length() != 3) {
            setError.postValue(errorCVVCode);
            showProgressStatus.postValue(false);
            return;
        }
        String[] regions = new String[]{"cherkasy", "smela", "kanev", "zoloto", "ph", "vatutino", "zven"};
        String baseUrl = context.getString(R.string.payment_baseUrl);
        baseUrl += regions[userData.getCity()];
        baseUrl += "?&amount=" + summ + "&account=" + userData.getAccount();
        repo.getPaymentRedirection(redirectDocument,baseUrl, cardNumber, mm, yy, cvv);
        Repository.getInstance().getMapUsersCharacteristic().removeObserver(this);
    }

    public void redirect(Document result) {
        redirectDocument.postValue(result);
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

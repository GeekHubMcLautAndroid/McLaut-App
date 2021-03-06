package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Locale;

import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;

public class TachcardPayViewModel extends ViewModel implements Observer<HashMap<String, UserCharacteristic>> {
    private UserInfoEntity userData;
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> setError = new MutableLiveData<>();
    private MutableLiveData<Document> redirectDocument = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    MutableLiveData<Boolean> getProgressStatusData() {
        return showProgressStatus;
    }

    MutableLiveData<Integer> getSetError() {
        return setError;
    }

    MutableLiveData<Document> getRedirectDocument() {
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

    void pay(Context context, String accountID, int city, String summ, String cardNumber, String mm, String yy, String cvv, boolean saveCard) {
        showProgressStatus.postValue(true);
        int[] digits = new int[cardNumber.length()];
        int errorBadAccount = -1;
        if (accountID.length() < 1) {
            setError.postValue(errorBadAccount);
            showProgressStatus.postValue(false);
            return;
        }
        int errorMinimumRefungCode = 0;
        if (summ.length() < 1) {
            setError.postValue(errorMinimumRefungCode);
            showProgressStatus.postValue(false);
            return;
        }
        double summDouble = Double.parseDouble(summ);
        if (summDouble < 5) {
            setError.postValue(errorMinimumRefungCode);
            showProgressStatus.postValue(false);
            return;
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
        }
        if (cardNumber.length() < 16 || !checkLuhn(digits)) {
            int errorCardNumberCode = 1;
            setError.postValue(errorCardNumberCode);
            showProgressStatus.postValue(false);
            return;
        }
        int errorMM_YYCode = 2;
        if (mm.length() < 2) {
            setError.postValue(errorMM_YYCode);
            showProgressStatus.postValue(false);
            return;
        }
        if (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12) {
            setError.postValue(errorMM_YYCode);
            showProgressStatus.postValue(false);
            return;
        }
        if (cvv.length() != 3) {
            int errorCVVCode = 3;
            setError.postValue(errorCVVCode);
            showProgressStatus.postValue(false);
            return;
        }
        if (saveCard) {
            if (!repo.getMapCardEntities().containsKey(cardNumber)) {
                repo.addNewCard(new CardInfoEntity(cardNumber, mm, yy));
            }
        }
        String[] regions = new String[]{"cherkasy", "smela", "kanev", "zoloto", "ph", "vatutino", "zven"};
        String baseUrl = context.getString(R.string.payment_baseUrl);
        baseUrl += regions[city];
        baseUrl += "?&amount=" + String.format(Locale.ENGLISH, "%.2f", summDouble) + "&account=" + accountID;
        repo.getPaymentRedirection(redirectDocument, baseUrl, cardNumber, mm, yy, cvv);
        Repository.getInstance().getMapUsersCharacteristic().removeObserver(this);
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

    HashMap<String, CardInfoEntity> getCards() {
        return repo.getMapCardEntities();
    }

    String getAccountID() {
        return userData.getAccount();
    }

    int getAccountCity() {
        return userData.getCity();
    }
}

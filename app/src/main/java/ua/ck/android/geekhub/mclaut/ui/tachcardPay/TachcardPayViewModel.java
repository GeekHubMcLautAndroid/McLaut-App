package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.jsoup.nodes.Document;

import ua.ck.android.geekhub.mclaut.data.Repository;

public class TachcardPayViewModel extends ViewModel {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> setError = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    public MutableLiveData<Boolean> getProgressStatusData() {
        return showProgressStatus;
    }

    public MutableLiveData<Integer> getSetError() {
        return setError;
    }

    public Document pay(String summ, String cardNumber, String mm, String yy, String cvv) {
        showProgressStatus.postValue(true);
        int userRegion = 0; //TODO:write get user region from bd;
        int[] digits = new int[cardNumber.length()];
        if (Double.parseDouble(summ) < 5) {
            setError.postValue(0);
            showProgressStatus.postValue(false);
            return null;
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
        }
        if (cardNumber.length() < 16 || checkLuhn(digits)) {
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
        String baseUrl = "https://user.tachcard.com/uk/pay-mclaut";
        switch (userRegion) {
            case 0:
                baseUrl += "cherkasy";
            case 1:
                baseUrl += "smela";
            case 2:
                baseUrl += "kanev";
            case 3:
                baseUrl += "zoloto";
            case 4:
                baseUrl += "ph";
            case 5:
                baseUrl += "vatutino";
            case 6:
                baseUrl += "zven";
        }
        baseUrl += "?&amount=" + summ + "&account="; //TODO:write get account;
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

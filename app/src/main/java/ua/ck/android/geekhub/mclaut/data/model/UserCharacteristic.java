package ua.ck.android.geekhub.mclaut.data.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sergo on 12/2/17.
 */

public class UserCharacteristic {
    public static final int PAYMENTS = 1;
    public static final int WITHDRAWALS = 0;

    private UserInfoEntity info;

    private List<CashTransactionsEntity> paymentsTransactions = new LinkedList<>();
    private List<CashTransactionsEntity> withdrawalsTransactions = new LinkedList<>();

    public UserCharacteristic(){

    }

    public UserCharacteristic(UserInfoEntity info,
                              List<CashTransactionsEntity> paymentsTransactions,
                              List<CashTransactionsEntity> withdrawalsTransactions) {

        this.info = info;
        this.paymentsTransactions = paymentsTransactions;
        this.withdrawalsTransactions = withdrawalsTransactions;
    }

    public UserCharacteristic(UserCharacteristic userCharacteristic){
        this.info = new UserInfoEntity(userCharacteristic.info);
        for(CashTransactionsEntity cashPayment : userCharacteristic.paymentsTransactions){
            this.paymentsTransactions.add(new CashTransactionsEntity(cashPayment, PAYMENTS));
        }
        for(CashTransactionsEntity cashWithdrawals : userCharacteristic.withdrawalsTransactions){
            this.withdrawalsTransactions.add(new CashTransactionsEntity(cashWithdrawals, WITHDRAWALS));
        }
    }

    public void setInfo(UserInfoEntity info) {
        this.info = info;
    }

    public UserInfoEntity getInfo() {
        return info;
    }

    public void setPaymentsTransactions(List<CashTransactionsEntity> paymentsTransactions) {
        this.paymentsTransactions = paymentsTransactions;
    }
    public List<CashTransactionsEntity> getPaymentsTransactions() {
        return paymentsTransactions;
    }

    public void setWithdrawalsTransactions(List<CashTransactionsEntity> withdrawalsTransactions) {
        this.withdrawalsTransactions = withdrawalsTransactions;
    }
    public List<CashTransactionsEntity> getWithdrawalsTransactions() {
        return withdrawalsTransactions;
    }
}

package ua.ck.android.geekhub.mclaut.data.model;

import java.util.List;

/**
 * Created by Sergo on 12/2/17.
 */

public class UserCharacteristic {

    private UserInfoEntity info;

    private List<CashTransactionsEntity> paymentsTransactions;
    private List<CashTransactionsEntity> withdrawalsTransactions;

    public UserCharacteristic(){

    }

    public UserCharacteristic(UserInfoEntity info,
                              List<CashTransactionsEntity> paymentsTransactions,
                              List<CashTransactionsEntity> withdrawalsTransactions) {

        this.info = info;
        this.paymentsTransactions = paymentsTransactions;
        this.withdrawalsTransactions = withdrawalsTransactions;
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

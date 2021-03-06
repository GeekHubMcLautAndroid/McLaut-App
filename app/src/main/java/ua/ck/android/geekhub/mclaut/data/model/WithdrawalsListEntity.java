package ua.ck.android.geekhub.mclaut.data.model;

import java.util.List;

// Only for Retrofit
public class WithdrawalsListEntity {
    private List<CashTransactionsEntity> withdrawals;
    private int localResCode;

    public WithdrawalsListEntity(List<CashTransactionsEntity> withdrawals) {
        this.withdrawals = withdrawals;
    }

    public WithdrawalsListEntity() {
    }

    public synchronized List<CashTransactionsEntity> getWithdrawals() {
        return withdrawals;
    }

    public int getLocalResCode() {
        return localResCode;
    }

    public void setLocalResCode(int localResCode) {
        this.localResCode = localResCode;
    }
}

package ua.ck.android.geekhub.mclaut.data.model;

import java.util.List;

// Only for Retrofit
public class PaymentsListEntity {
    private List<CashTransactionsEntity> payments;
    private int localResCode;

    public PaymentsListEntity(List<CashTransactionsEntity> payments) {
        this.payments = payments;
    }

    public PaymentsListEntity() {
    }

    public synchronized List<CashTransactionsEntity> getPayments() {
        return payments;
    }

    public int getLocalResCode() {
        return localResCode;
    }

    public void setLocalResCode(int localResCode) {
        this.localResCode = localResCode;
    }
}

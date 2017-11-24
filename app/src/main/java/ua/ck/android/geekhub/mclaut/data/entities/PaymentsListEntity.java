package ua.ck.android.geekhub.mclaut.data.entities;

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

    public List<CashTransactionsEntity> getPayments() {
        return payments;
    }

    public int getLocalResCode() {
        return localResCode;
    }

    public void setLocalResCode(int localResCode) {
        this.localResCode = localResCode;
    }
}

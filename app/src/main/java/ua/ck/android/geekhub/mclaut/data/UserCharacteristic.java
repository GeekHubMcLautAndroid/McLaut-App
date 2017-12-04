package ua.ck.android.geekhub.mclaut.data;

import java.util.List;

import ua.ck.android.geekhub.mclaut.data.entities.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;

/**
 * Created by Sergo on 12/2/17.
 */

public class UserCharacteristic {

    private UserInfoEntity info;

    private List<UserConnectionsInfo> connections;

    private List<CashTransactionsEntity> paymentsTransactions;
    private List<CashTransactionsEntity> withdrawalsTransactions;

    public UserCharacteristic(){

    }

    public UserCharacteristic(UserInfoEntity info,
                              List<UserConnectionsInfo> connections,
                              List<CashTransactionsEntity> paymentsTransactions,
                              List<CashTransactionsEntity> withdrawalsTransactions) {

        this.info = info;
        this.connections = connections;
        this.paymentsTransactions = paymentsTransactions;
        this.withdrawalsTransactions = withdrawalsTransactions;
    }


    public void setInfo(UserInfoEntity info) {
        this.info = info;
    }
    public UserInfoEntity getInfo() {
        return info;
    }

    public void setConnections(List<UserConnectionsInfo> connections) {
        this.connections = connections;
    }
    public List<UserConnectionsInfo> getConnections() {
        return connections;
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

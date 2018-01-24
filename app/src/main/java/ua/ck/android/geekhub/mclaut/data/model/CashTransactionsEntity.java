package ua.ck.android.geekhub.mclaut.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;
/**
 * Created by Sergo on 11/20/17.
 */

@Entity(tableName = "cashTransactions",
        foreignKeys = @ForeignKey(entity = UserInfoEntity.class,
                                        parentColumns = "id",
                                        childColumns = "idClient",
                                        onDelete = CASCADE),
        primaryKeys = {"date","idClient"}
)
public class CashTransactionsEntity {

    public static final int PAYMENTS = 1;
    public static final int WITHDRAWALS = 0;

    private int type;
    private int typeOfTransaction;
    @NonNull
    private Long date;
    private double sum;
    @SerializedName("id_client")
    @NonNull
    private String idClient;
    @SerializedName("sum_before")
    private double sumBefore;

    public CashTransactionsEntity(int type, String idClient, Long date, double sum, double sumBefore) {
        this.sum = sum;
        this.type = type;
        this.date = date;
        this.idClient = idClient;
        this.sumBefore = sumBefore;
    }

    public CashTransactionsEntity(CashTransactionsEntity cashTransactionsEntity) {
        this.sum = cashTransactionsEntity.sum;
        this.type = cashTransactionsEntity.type;
        this.date = cashTransactionsEntity.date;
        this.idClient = cashTransactionsEntity.idClient;
        this.sumBefore = cashTransactionsEntity.sumBefore;
    }

    public CashTransactionsEntity(CashTransactionsEntity cashTransactionsEntity, int typeOfTransaction) {
        this.sum = cashTransactionsEntity.sum;
        this.type = cashTransactionsEntity.type;
        this.date = cashTransactionsEntity.date;
        this.idClient = cashTransactionsEntity.idClient;
        this.sumBefore = cashTransactionsEntity.sumBefore;
        this.typeOfTransaction = typeOfTransaction;
    }

    public int getType() {
        return type;
    }

    public double getSum() {
        return sum;
    }

    public Long getDate() {
        return date;
    }

    public String getIdClient() {
        return idClient;
    }

    public double getSumBefore() {
        return sumBefore;
    }

    public int getTypeOfTransaction() {
        return typeOfTransaction;
    }

    ///
    public void setTypeOfTransaction(int type){
        if(type == PAYMENTS){
            this.typeOfTransaction = PAYMENTS;
            return;
        }
        this.typeOfTransaction = WITHDRAWALS;
    }

}

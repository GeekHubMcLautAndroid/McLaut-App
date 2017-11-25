package ua.ck.android.geekhub.mclaut.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Sergo on 11/20/17.
 */

@Entity(tableName = "cashTransactions")
public class CashTransactionsEntity {

    public static final int PAYMENTS = 1;
    public static final int WITHDRAWALS = 0;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private int typeOfTransaction;

    private Long date;
    private double sum;
    private String idClient;
    private double sumBefore;

    public CashTransactionsEntity(int id, int type, String idClient, Long date, double sum, double sumBefore) {
        this.id = id;
        this.sum = sum;
        this.type = type;
        this.date = date;
        this.idClient = idClient;
        this.sumBefore = sumBefore;
    }

    public int getId() {
        return id;
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

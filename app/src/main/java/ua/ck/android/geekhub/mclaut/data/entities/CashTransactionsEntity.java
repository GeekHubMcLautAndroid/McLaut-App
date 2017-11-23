package ua.ck.android.geekhub.mclaut.data.entities;

import android.arch.persistence.room.Entity;
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
    private String idClient;
    private Date date;
    private double sum;
    private double sumBefore;

    public CashTransactionsEntity(int typeOfTransaction ,int id, int type, String idClient, Date date, double sum, double sumBefore) {
        this.id = id;
        this.type = type;
        this.typeOfTransaction = typeOfTransaction;
        this.idClient = idClient;
        this.date = date;
        this.sum = sum;
        this.sumBefore = sumBefore;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public String getIdClient() {
        return idClient;
    }

    public Date getDate() {
        return date;
    }

    public double getSum() {
        return sum;
    }

    public double getSumBefore() {
        return sumBefore;
    }
}

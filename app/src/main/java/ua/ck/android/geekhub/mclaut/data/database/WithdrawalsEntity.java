package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;



/**
 * Created by bogda on 15.11.2017.
 */

@Entity(tableName = "withdrawals")
public class WithdrawalsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private String idClient;
    private Date date;
    private double sum;
    private double sumBefore;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSumBefore() {
        return sumBefore;
    }

    public void setSumBefore(double sumBefore) {
        this.sumBefore = sumBefore;
    }
}

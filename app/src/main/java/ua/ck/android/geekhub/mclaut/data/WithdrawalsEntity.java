package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.sql.Date;

/**
 * Created by bogda on 15.11.2017.
 */

@Entity
public class WithdrawalsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private String idClient;
    @TypeConverters(DateConverter.class)
    private Date date;
    private double sum;
    private double sumBefore;


    public int getId() {
        return id;
    }

    public int getType() {
        return type;
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

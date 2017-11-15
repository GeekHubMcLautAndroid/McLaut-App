package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.sql.Date;

/**
 * Created by bogda on 15.11.2017.
 */
@Entity(tableName = "payments")
public class PaymentsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private String idClient;
    @TypeConverters(DateConverter.class)
    private Date date;
    private double sum;
    private double sumBefore;

    public PaymentsEntity(int id, int type, String idClient, Date date, double sum, double sumBefore) {
        this.id = id;
        this.type = type;
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

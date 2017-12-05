package ua.ck.android.geekhub.mclaut.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import ua.ck.android.geekhub.mclaut.data.database.LocalDatabase;
import ua.ck.android.geekhub.mclaut.tools.McLautAppExecutor;

/**
 * Created by Sergo on 11/26/17.
 */

@Entity(tableName = "cardEntity")
public class CardInfoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String endYear;
    private String endMonth;
    private String cardNumber;

    private int counterOfUses;

    public CardInfoEntity(String cardNumber, String endMonth, String endYear) {
        this.cardNumber = cardNumber;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.counterOfUses = 0;
    }

    public void setId(int id) { this.id = id; }

    public void setCounterOfUses(int counterOfUses) { this.counterOfUses = counterOfUses; }

    public int getId() {return id; }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getEndMonth(){
        return endMonth;
    }

    public String getEndYear(){
        return endYear;
    }

    public int getCounterOfUses() { return counterOfUses; }

    public void incrementCounterOfUses(Context context) {
        counterOfUses++;
        update(context);
    }

    public void updateCardNumber(Context context, String cardNumber){
        this.cardNumber = cardNumber;
        update(context);
    }

    public void updateEndMonth(Context context, String endMonth) {
        this.endMonth = endMonth;
        update(context);
    }

    public void updateEndYear(Context context, String endYear) {
        this.endMonth = endYear;
        update(context);
    }

    private void update(Context context){
       McLautAppExecutor executor = McLautAppExecutor.getInstance();

        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(context).dao()
                    .updateCardEntity(this);
        });
    }

}

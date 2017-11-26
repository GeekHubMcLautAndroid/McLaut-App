package ua.ck.android.geekhub.mclaut.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.database.LocalDatabase;

/**
 * Created by Sergo on 11/26/17.
 */

@Entity(tableName = "cardEntity")
public class CardInfoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String endYear;
    private String endMouth;
    private String cardNumber;

    private int counterOfUses;

    public CardInfoEntity(String cardNumber, String endMouth, String endYear) {
        this.cardNumber = cardNumber;
        this.endMouth = endMouth;
        this.endYear = endYear;
        this.counterOfUses = 0;
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getEndMouth(){
        return endMouth;
    }

    public String getEndYear(){
        return endYear;
    }

    public void incrementCounterOfUses(Context context){
        counterOfUses++;
        LocalDatabase.getInstance(context).dao()
                .updateCardEntity(this);
    }

}

package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * Created by bogda on 13.11.2017.
 */
@Entity(tableName = "userInfo")
public class UserInfoEntity {
    @PrimaryKey
    private String id;
    private String login;
    private String isActive;
    private String account;
    private String balance;
    private String name;

    @TypeConverters(DateConverter.class)
    private Date paymentDateLast;

    @TypeConverters(DateConverter.class)
    private Date withdrawDateLast;

    @Relation(parentColumn = "id", entityColumn = "idClient", entity = UserConnectionsInfo.class)
    private List<UserConnectionsInfo> connectionsInfos = null;

    public UserInfoEntity(String id, String login, String isActive, String account, String balance,
                          String name, Date paymentDateLast, Date withdrawDateLast,
                          List<UserConnectionsInfo> connectionsInfos) {
        this.id = id;
        this.login = login;
        this.isActive = isActive;
        this.account = account;
        this.balance = balance;
        this.name = name;
        this.paymentDateLast = paymentDateLast;
        this.withdrawDateLast = withdrawDateLast;
        this.connectionsInfos = connectionsInfos;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getAccount() {
        return account;
    }

    public String getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public List<UserConnectionsInfo> getConnectionsInfos() {
        return connectionsInfos;
    }

    public Date getPaymentDateLast() {
        return paymentDateLast;
    }

    public Date getWithdrawDateLast() {
        return withdrawDateLast;
    }
}
 @Entity(tableName = "userConnectionsInfo")
 class UserConnectionsInfo{
    @PrimaryKey
    private String id;
    private String idClient;
    private String isActive;
    private String login;
    private String payAtDay;
    private String tariff;

     public UserConnectionsInfo(String id, String idClient, String isActive, String login,
                                String payAtDay, String tariff) {
         this.id = id;
         this.idClient = idClient;
         this.isActive = isActive;
         this.login = login;
         this.payAtDay = payAtDay;
         this.tariff = tariff;
     }

     public String getId() {
         return id;
     }

     public String getIdClient() {
         return idClient;
     }

     public String getIsActive() {
         return isActive;
     }

     public String getLogin() {
         return login;
     }

     public String getPayAtDay() {
         return payAtDay;
     }

     public String getTariff() {
         return tariff;
     }
 }

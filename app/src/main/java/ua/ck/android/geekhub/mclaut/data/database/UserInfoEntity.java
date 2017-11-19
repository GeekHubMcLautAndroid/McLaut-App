package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by bogda on 13.11.2017.
 */
@Entity(tableName = "userInfo")
public class UserInfoEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String login;
    private String isActive;
    private String account;
    private String balance;
    private String name;
    private Date paymentDateLast;
    private Date withdrawDateLast;


    public UserInfoEntity(@NonNull String id, String login, String isActive, String account, String balance,
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
    }

    public UserInfoEntity() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPaymentDateLast() {
        return paymentDateLast;
    }

    public void setPaymentDateLast(Date paymentDateLast) {
        this.paymentDateLast = paymentDateLast;
    }

    public Date getWithdrawDateLast() {
        return withdrawDateLast;
    }

    public void setWithdrawDateLast(Date withdrawDateLast) {
        this.withdrawDateLast = withdrawDateLast;
    }
}

@Entity(tableName = "userConnectionsInfo", foreignKeys = @ForeignKey(onDelete = CASCADE,
        entity = UserInfoEntity.class, parentColumns = "id", childColumns = "idClient"))
 class UserConnectionsInfo{
    @PrimaryKey
    @NonNull
    private String id;
    private String idClient;
    private String isActive;
    private String login;
    private String payAtDay;
    private String tariff;

    public UserConnectionsInfo(@NonNull String id, String idClient, String isActive, String login,
                               String payAtDay, String tariff) {
         this.id = id;
         this.idClient = idClient;
         this.isActive = isActive;
         this.login = login;
         this.payAtDay = payAtDay;
         this.tariff = tariff;
     }

    @NonNull
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

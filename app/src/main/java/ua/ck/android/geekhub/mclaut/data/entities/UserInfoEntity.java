package ua.ck.android.geekhub.mclaut.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


/**
 * Created by bogda on 13.11.2017.
 */
@Entity(tableName = "userInfo")
public class UserInfoEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private int city;
    private String name;
    private String login;
    private String account;
    private String balance;
    @SerializedName("is_active")
    private String isActive;
    @Ignore
    private int localResCode;
    private String certificate;
    @SerializedName("payment_date_last")
    private Long paymentDateLast;
    @SerializedName("withdrawal_date_last")
    private Long withdrawDateLast;
    @Ignore
    @SerializedName("users")
    private List<UserConnectionsInfo> userConnectionsInfo;


    public UserInfoEntity(@NonNull String id, String login, String isActive, String account, String balance,
                          String name, Long paymentDateLast, Long withdrawDateLast
                         ) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.account = account;
        this.balance = balance;
        this.isActive = isActive;
        this.paymentDateLast = paymentDateLast;
        this.withdrawDateLast = withdrawDateLast;
    }

    @Ignore
    public UserInfoEntity() {
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
    @NonNull
    public String getId() {
        return id;
    }
    ///

    public void setLogin(String login) {
        this.login = login;
    }
    @NonNull
    public String getLogin() {
        return login;
    }
    ///

    public void setCertificate(String certificate) {this.certificate = certificate; }
    public String getCertificate() {return certificate; }
    ///

    public void setCity(int city) {this.city = city; }
    public int getCity() {return city; }
    ///

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    public String getIsActive() {
        return isActive;
    }
    ///

    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }
    ///

    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getBalance() {
        return balance;
    }
    ///

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    ///

    public void setPaymentDateLast(Long paymentDateLast) {
        this.paymentDateLast = paymentDateLast;
    }
    public Long getPaymentDateLast() {
        return paymentDateLast;
    }
    ///

    public void setWithdrawDateLast(Long withdrawDateLast) {
        this.withdrawDateLast = withdrawDateLast;
    }
    public Long getWithdrawDateLast() {
        return withdrawDateLast;
    }
    ///

    public void setLocalResCode(int localResCode) {
        this.localResCode = localResCode;
    }
    public int getLocalResCode() {
        return localResCode;
    }
    ///

    public void setUserConnectionsInfoList(List<UserConnectionsInfo> userConnectionsInfo) {
        this.userConnectionsInfo = userConnectionsInfo;
    }
    public List<UserConnectionsInfo> getUserConnectionsInfoList() {
        return userConnectionsInfo;
    }


    ///
    ///
    ///
    public Date getConvertedPaymentDateLast() {
        return new Date(paymentDateLast * 1000);
    }

    public Date getConvertedWithdrawDateLast() {
        return new Date(withdrawDateLast * 1000);
    }

}



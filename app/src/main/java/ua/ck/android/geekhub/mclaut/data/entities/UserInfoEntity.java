package ua.ck.android.geekhub.mclaut.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("is_active")
    private String isActive;
    private String account;
    private String balance;
    private String name;
    @SerializedName("payment_date_last")
    private Date paymentDateLast;
    @SerializedName("withdrawal_date_last")
    private Date withdrawDateLast;
    @Ignore
    private List<UserConnectionsInfo> userConnectionsInfoList;
    @Ignore
    private int localResCode;

    public UserInfoEntity(@NonNull String id, String login, String isActive, String account, String balance,
                          String name, Date paymentDateLast, Date withdrawDateLast
                         ) {
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

    public Date getPaymentDateLast() {
        return paymentDateLast;
    }

    public Date getWithdrawDateLast() {
        return withdrawDateLast;
    }

    public List<UserConnectionsInfo> getUserConnectionsInfoList() {
        return userConnectionsInfoList;
    }

    public int getLocalResCode() {
        return localResCode;
    }

    public void setUserConnectionsInfoList(List<UserConnectionsInfo> userConnectionsInfoList) {
        this.userConnectionsInfoList = userConnectionsInfoList;
    }

    public void setLocalResCode(int localResCode) {
        this.localResCode = localResCode;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaymentDateLast(Date paymentDateLast) {
        this.paymentDateLast = paymentDateLast;
    }

    public void setWithdrawDateLast(Date withdrawDateLast) {
        this.withdrawDateLast = withdrawDateLast;
    }
}



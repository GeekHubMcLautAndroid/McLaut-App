package ua.ck.android.geekhub.mclaut.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by bogda on 19.11.2017.
 */

@Entity(tableName = "userConnectionsInfo",
        foreignKeys = @ForeignKey(onDelete = CASCADE,
                                    entity = UserInfoEntity.class,
                                             parentColumns = "id",
                                             childColumns = "idClient"))
public class UserConnectionsInfo{

    private String id;
    @SerializedName("id_client")
    @PrimaryKey
    @NonNull
    private String idClient;
    @SerializedName("is_active")
    private String isActive;
    private String login;
    @SerializedName("pay_at_day")
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
package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by bogda on 13.11.2017.
 */
@Entity
public class UserInfoEntity {
    @PrimaryKey
    private String id;
    private String login;
    private String isActive;
    private String account;
    private String balance;
    private String name;
    //TODO: create other fields
}

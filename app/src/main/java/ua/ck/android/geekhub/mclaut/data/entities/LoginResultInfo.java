package ua.ck.android.geekhub.mclaut.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bogda on 19.11.2017.
 */

public class LoginResultInfo {
    @SerializedName("result")
    private int resultCode;
    private String certificate;
    private int localResultCode;

    public LoginResultInfo(int result, String certificate, int localResultCode) {
        this.resultCode = result;
        this.certificate = certificate;
        this.localResultCode = localResultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getCertificate() {
        return certificate;
    }

    public int getLocalResultCode() {
        return localResultCode;
    }

}

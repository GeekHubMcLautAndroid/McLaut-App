package ua.ck.android.geekhub.mclaut.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.ck.android.geekhub.mclaut.data.model.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.model.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.model.WithdrawalsListEntity;



public interface MclautMethodsInterface {
    @GET("api.php?")
    Call<LoginResultInfo> login(@Query("hash") String hash);

    @GET("api.php?")
    Call<UserInfoEntity> getUserInfo(@Query("hash") String hash);

    @GET("api.php")
    Call<WithdrawalsListEntity> getWithdrawals(@Query("hash") String hash);

    @GET("api.php")
    Call<PaymentsListEntity> getPayments(@Query("hash") String hash);
}

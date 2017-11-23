package ua.ck.android.geekhub.mclaut.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;

/**
 * Created by bogda on 19.11.2017.
 */

public interface MclautMethodsInterface {
    @GET("api.php?")
    Call<LoginResultInfo> login(@Query("hash") String hash);

    @GET("api.php?")
    Call<UserInfoEntity> getUserInfo(@Query("hash") String hash);

}

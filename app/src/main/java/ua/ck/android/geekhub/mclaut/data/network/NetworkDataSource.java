package ua.ck.android.geekhub.mclaut.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.tools.McLautHashGenerator;

/**
 * Created by bogda on 19.11.2017.
 */

public class NetworkDataSource {
    private static NetworkDataSource instance;
    public static final int RESPONSE_FAILTURE_CODE = -100;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://app.mclaut.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MclautMethodsInterface mclautMethodsInterface;

    private NetworkDataSource() {
    }

    public static NetworkDataSource getInstance() {
        if (instance == null) {
            instance = new NetworkDataSource();
        }
        return instance;
    }


    public MutableLiveData<LoginResultInfo> checkLogin(String login, String password, int city) {
        mclautMethodsInterface = retrofit.create(MclautMethodsInterface.class);
        String hash = McLautHashGenerator.generateLoginHash(login, password, city);

        final MutableLiveData<LoginResultInfo> result = new MutableLiveData<>();

        mclautMethodsInterface.login(hash).enqueue(new Callback<LoginResultInfo>() {
            @Override
            public void onResponse(Call<LoginResultInfo> call, Response<LoginResultInfo> response) {
                String certificate = response.body().getCertificate();
                int localResCode = certificate.equals("0") ? 0 : 1;
                LoginResultInfo res = new LoginResultInfo(response.body().getResultCode(),
                        response.body().getCertificate(),localResCode);
                result.postValue(res);
            }

            @Override
            public void onFailure(Call<LoginResultInfo> call, Throwable t) {
                LoginResultInfo info = new LoginResultInfo(0,"",RESPONSE_FAILTURE_CODE);
                result.postValue(info);
            }
        });
        return result;
    }
}

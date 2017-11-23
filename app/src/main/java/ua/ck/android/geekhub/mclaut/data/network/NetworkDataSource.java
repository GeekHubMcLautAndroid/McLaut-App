package ua.ck.android.geekhub.mclaut.data.network;

import android.arch.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.entities.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.WithdrawalsListEntity;
import ua.ck.android.geekhub.mclaut.tools.McLautHashGenerator;

/**
 * Created by bogda on 19.11.2017.
 */

public class NetworkDataSource {
    private static NetworkDataSource instance;
    public static final int RESPONSE_FAILURE_CODE = -100;
    public static final int RESPONSE_SUCCESSFUL_CODE = 1;
    public static final int RESPONSE_BAD_RESULT_CODE = 0;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://app.mclaut.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MclautMethodsInterface mclautMethodsInterface;

    private NetworkDataSource() {
        mclautMethodsInterface = retrofit.create(MclautMethodsInterface.class);
    }

    public static NetworkDataSource getInstance() {
        if (instance == null) {
            instance = new NetworkDataSource();
        }
        return instance;
    }


    public MutableLiveData<LoginResultInfo> checkLogin(String login, String password, int city) {

        final MutableLiveData<LoginResultInfo> result = new MutableLiveData<>();

        String hash = McLautHashGenerator.generateLoginHash(login, password, city);

        mclautMethodsInterface.login(hash).enqueue(new Callback<LoginResultInfo>() {
            @Override
            public void onResponse(Call<LoginResultInfo> call, Response<LoginResultInfo> response) {
                String certificate = response.body().getCertificate();
                int localResCode = certificate.equals("0") ? RESPONSE_BAD_RESULT_CODE : RESPONSE_SUCCESSFUL_CODE;
                LoginResultInfo res = new LoginResultInfo(response.body().getResultCode(),
                        response.body().getCertificate(),localResCode);
                result.postValue(res);
            }

            @Override
            public void onFailure(Call<LoginResultInfo> call, Throwable t) {
                LoginResultInfo info = new LoginResultInfo(0,"", RESPONSE_FAILURE_CODE);
                result.postValue(info);
            }
        });
        return result;
    }

    public MutableLiveData<UserInfoEntity> getUserInfo(String certificate, int city){
        final MutableLiveData<UserInfoEntity> result = new MutableLiveData<>();

        String hash = McLautHashGenerator.getUserInfo(certificate,city);

        mclautMethodsInterface.getUserInfo(hash).enqueue(new Callback<UserInfoEntity>() {
            @Override
            public void onResponse(Call<UserInfoEntity> call, Response<UserInfoEntity> response) {
                UserInfoEntity resData = response.body();
                resData.setLocalResCode(RESPONSE_SUCCESSFUL_CODE);
                result.postValue(resData);
            }

            @Override
            public void onFailure(Call<UserInfoEntity> call, Throwable t) {
                UserInfoEntity resData = new UserInfoEntity();
                resData.setLocalResCode(RESPONSE_FAILURE_CODE);
                result.postValue(resData);
            }
        });
        return result;
    }

    public MutableLiveData<WithdrawalsListEntity> getWithdrawals(String certificate, int city){
        final MutableLiveData<WithdrawalsListEntity> result = new MutableLiveData<>();

        String hash = McLautHashGenerator.getWithdrawalsInfo(certificate,city);

        mclautMethodsInterface.getWithdrawals(hash).enqueue(new Callback<WithdrawalsListEntity>() {
            @Override
            public void onResponse(Call<WithdrawalsListEntity> call, Response<WithdrawalsListEntity> response) {
                WithdrawalsListEntity resData = response.body();
                resData.setLocalResCode(RESPONSE_SUCCESSFUL_CODE);
                result.postValue(resData);
            }

            @Override
            public void onFailure(Call<WithdrawalsListEntity> call, Throwable t) {
                WithdrawalsListEntity resData = new WithdrawalsListEntity();
                resData.setLocalResCode(RESPONSE_FAILURE_CODE);
                result.postValue(resData);
            }
        });
        return result;
    }
    public MutableLiveData<PaymentsListEntity> getPayments(String certificate, int city){
        final MutableLiveData<PaymentsListEntity> result = new MutableLiveData<>();

        String hash = McLautHashGenerator.getPaymentsInfo(certificate,city);

        mclautMethodsInterface.getPayments(hash).enqueue(new Callback<PaymentsListEntity>() {
            @Override
            public void onResponse(Call<PaymentsListEntity> call, Response<PaymentsListEntity> response) {
                PaymentsListEntity resData = response.body();
                resData.setLocalResCode(RESPONSE_SUCCESSFUL_CODE);
                result.postValue(resData);
            }

            @Override
            public void onFailure(Call<PaymentsListEntity> call, Throwable t) {
                PaymentsListEntity resData = new PaymentsListEntity();
                resData.setLocalResCode(RESPONSE_FAILURE_CODE);
                result.postValue(resData);
            }
        });
        return result;
    }
}

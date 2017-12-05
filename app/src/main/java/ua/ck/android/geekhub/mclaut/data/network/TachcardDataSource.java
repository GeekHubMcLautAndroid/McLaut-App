package ua.ck.android.geekhub.mclaut.data.network;

import android.arch.lifecycle.MutableLiveData;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Nimolee on 26-Nov-17.
 */

public class TachcardDataSource {
    private static TachcardDataSource instance;

    private TachcardDataSource() {
    }

    public static TachcardDataSource getInstance() {
        if (instance == null) {
            instance = new TachcardDataSource();
        }
        return instance;
    }

    public MutableLiveData<Document> pay(String... strings) {
        Connection.Response main;
        Element secondStage;
        String tc_user_session;
        try {
            main = Jsoup.connect(strings[0]).userAgent("Mozilla").execute();
            secondStage = main.parse().getElementById("secondStage");
            tc_user_session = main.cookie("tc_user_session");
        } catch (IOException e) {
            return null;
        }
        Elements hiddenInputs = secondStage.select("input[type=hidden]");
        String pan = strings[1];
        String mm = strings[2];
        String yy = strings[3];
        String cvv = strings[4];
        Document result;
        try {
            result = Jsoup.connect("https://user.tachcard.com/uk/pay-from-card")
                    .data(hiddenInputs.get(0).attr("name"), hiddenInputs.get(0).attr("value"))
                    .data(hiddenInputs.get(1).attr("name"), "1")
                    .data(hiddenInputs.get(2).attr("name"), hiddenInputs.get(2).attr("value"))
                    .data("pan", pan)
                    .data("mm", mm)
                    .data("yy", yy)
                    .data("cvv", cvv)
                    .cookie("tc_user_session", tc_user_session)
                    .userAgent("Mozilla")
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .post();
        } catch (IOException e) {
            return null;
        }
        MutableLiveData<Document> resultMLD = new MutableLiveData<>();
        resultMLD.postValue(result);
        return resultMLD;
    }
}

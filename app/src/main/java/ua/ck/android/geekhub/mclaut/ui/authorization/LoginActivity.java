package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import butterknife.BindView;
import ua.ck.android.geekhub.mclaut.R;

public class LoginActivity extends AppCompatActivity {

    //@BindView(R.id.login_activity_city_spinner)
    //Spinner cityesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }
}
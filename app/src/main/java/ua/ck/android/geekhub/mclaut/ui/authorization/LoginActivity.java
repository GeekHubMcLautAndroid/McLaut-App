package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.ck.android.geekhub.mclaut.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_activity_city_spinner)
    Spinner cityesSpinner;
    @BindView(R.id.login_activity_login_text_input_layout)
    TextInputLayout loginTextInputLayout;
    @BindView(R.id.login_activity_login_text_input_edittext)
    TextInputEditText loginTextInputEditText;
    @BindView(R.id.login_activity_password_text_input_layout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.login_activity_password_text_input_edittext)
    TextInputEditText passwordEditText;
    @BindView(R.id.login_activity_button_sign_in)
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_activity_button_sign_in)
    public void logInButtonClick(){
        //TODO: Login procedure
    }
}
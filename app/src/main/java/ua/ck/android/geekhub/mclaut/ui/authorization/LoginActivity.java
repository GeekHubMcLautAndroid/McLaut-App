package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.ui.MainActivity;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    public static final int RESPONSE_FAILTURE_CODE = -100;
    public static final int RESPONSE_SUCCESSFUL_CODE = 1;
    public static final int RESPONSE_BAD_RESULT_CODE = 0;
    public static final String ADD_NEW_USER = "_add_new_user";

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
    CircularProgressButton buttonLogin;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        viewModel.getProgressStatusData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    showProgress();
                }
                else {
                    hideProgress();
                }
            }
        });
        Boolean loginActivityParam = this.getIntent()
                .getBooleanExtra(ADD_NEW_USER, false);

        viewModel.initCharacteristicMap(loginActivityParam)
                .observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    continueAuthorization();
                } else {
                    startMainActivity();
                }
            }
        });
    }

    private void continueAuthorization(){
        loginTextInputEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        viewModel.getResultStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer){
                    case RESPONSE_SUCCESSFUL_CODE:
                        viewModel.getResultLoadDataAboutUserFromInternet().observe(LoginActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(@Nullable Boolean aBoolean) {
                                if(aBoolean){
                                    startMainActivity();
                                }
                            }
                        });
                        break;
                    case RESPONSE_BAD_RESULT_CODE:
                        passwordEditText.getText().clear();
                        passwordTextInputLayout.setError(getString(R.string.error_login));
                        passwordTextInputLayout.setErrorEnabled(true);
                        hideProgress();
                        break;
                    case RESPONSE_FAILTURE_CODE:
                        passwordEditText.getText().clear();
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.error_no_internet),
                                Toast.LENGTH_SHORT).show();
                        hideProgress();
                }
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (i == EditorInfo.IME_ACTION_DONE)) {
                    login();
                }
                return false;
            }
        });
    }

    @OnClick(R.id.login_activity_button_sign_in)
    public void logInButtonClick(){
        login();
    }

    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void login(){
        String login = loginTextInputEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        int city = cityesSpinner.getSelectedItemPosition();
        boolean error = false;
        if(login.equals("")) {
            loginTextInputLayout.setError(getString(R.string.error_empty_field));
            loginTextInputLayout.setErrorEnabled(true);
            error = true;
        }
        if(password.equals("")){
            passwordTextInputLayout.setError(getString(R.string.error_empty_field));
            passwordTextInputLayout.setErrorEnabled(true);
            error = true;
        }
        if(!error) {
            viewModel.login(login, password, city);
        }

    }

    public void showProgress(){
        buttonLogin.startAnimation();
        cityesSpinner.setEnabled(false);
        loginTextInputLayout.setEnabled(false);
        passwordTextInputLayout.setEnabled(false);
    }
    public  void hideProgress(){
        buttonLogin.revertAnimation();
        cityesSpinner.setEnabled(true);
        loginTextInputLayout.setEnabled(true);
        passwordTextInputLayout.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        passwordTextInputLayout.setErrorEnabled(false);
        loginTextInputLayout.setErrorEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
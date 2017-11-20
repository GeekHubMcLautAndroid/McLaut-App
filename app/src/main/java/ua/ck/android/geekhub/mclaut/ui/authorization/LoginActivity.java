package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

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

    private Context ctx = this;
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
        passwordEditText.addTextChangedListener(this);
        viewModel.getResultStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer){
                    case 1:
                        Intent intent = new Intent(ctx,MainActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        passwordEditText.getText().clear();
                        passwordTextInputLayout.setError(getString(R.string.error_login));
                        passwordTextInputLayout.setErrorEnabled(true);
                }
            }
        });
    }

    @OnClick(R.id.login_activity_button_sign_in)
    public void logInButtonClick(){
        viewModel.login(loginTextInputEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        cityesSpinner.getSelectedItemPosition());
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
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
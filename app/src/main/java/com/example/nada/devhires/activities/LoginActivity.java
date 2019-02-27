package com.example.nada.devhires.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nada.devhires.R;
import com.example.nada.devhires.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_editText_email)
    EditText editText_email;

    @BindView(R.id.login_editText_password)
    EditText editText_password;

    @BindView(R.id.login_button)
    Button button_login;

    @BindView(R.id.login_text_register)
    TextView textView_register;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    // Vars
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.login_button)
    public void onClickLoginButton() {
        login();
    }

    @OnClick(R.id.login_text_register)
    public void onClickRegisterText() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        LoginActivity.this.finish();
    }

    private void login() {
        if (isValidData()) {
            avi.show();
            String email = editText_email.getText().toString();
            String password = editText_password.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "wrong email or password", Toast.LENGTH_SHORT).show();
                            }
                            avi.hide();
                        }
                    });
        }

    }

    private boolean isValidData() {
        if (!Utils.isEmptyEditTexts(LoginActivity.this, editText_email, editText_password)) {
            if (Utils.isValidEmail(editText_email)) {
                if (Utils.isValidPassword(editText_password)) {
                    return true;
                }
            }
        }
        return false;
    }

}

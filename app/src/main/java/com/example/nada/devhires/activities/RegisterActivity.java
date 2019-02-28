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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_editText_email)
    EditText editText_email;

    @BindView(R.id.register_editText_password)
    EditText editText_password;

    @BindView(R.id.register_editText_confirmPassword)
    EditText editText_confirmPassword;

    @BindView(R.id.register_button)
    Button button_register;

    @BindView(R.id.register_text_login)
    TextView textView_login;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;


    //Vars
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.register_button)
    public void onClickRegisterButton() {
        Register();
    }

    @OnClick(R.id.register_text_login)
    public void onClickLoginText() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        RegisterActivity.this.finish();
    }

    private void Register() {
        if (isValidData()) {
            avi.show();
            final String email = editText_email.getText().toString();
            String password = editText_password.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        createUserDatabase(email);
                        avi.hide();
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.this_email_is_used_before), Toast.LENGTH_SHORT).show();
                        avi.hide();
                    }
                }
            });
        }
    }

    private void createUserDatabase(String Email) {
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "");
        userInfo.put("email", Email);
        userInfo.put("job_title", "");
        userInfo.put("company", "");
        userInfo.put("phone", "");
        userInfo.put("marital_status", "");
        userInfo.put("bio", "");
        userInfo.put("github_username", "");

        usersDatabase.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                avi.hide();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                RegisterActivity.this.finish();
            }
        });
    }

    private boolean isValidData() {
        if (!Utils.isEmptyEditTexts(RegisterActivity.this, editText_email, editText_password, editText_confirmPassword)) {
            if (Utils.isValidEmail(editText_email)) {
                if (Utils.isValidPassword(editText_password)) {
                    if (Utils.isMatched(editText_password, editText_confirmPassword)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

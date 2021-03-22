package com.starlord.runnigatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    Button signup;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signup = findViewById(R.id.welcome_signup);
        login = findViewById(R.id.welcome_login);

        signup.setOnClickListener(v -> {
            Intent intent =  new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        login.setOnClickListener(v -> {
            Intent intent =  new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
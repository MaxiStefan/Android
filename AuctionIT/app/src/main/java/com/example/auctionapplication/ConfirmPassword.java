package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConfirmPassword extends AppCompatActivity {
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Button reset;
    private Button cancel;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);

        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputConfirmPassword);

        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        reset = (Button) findViewById(R.id.reset);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmPassword.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");

        reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                updatePassword();

            }
        });
    }

    private void updatePassword() {

        String value1 = textInputEditTextPassword.getText().toString().trim();
        String value2 = textInputEditTextConfirmPassword.getText().toString().trim();

        if (value1.isEmpty() && value2.isEmpty()){
            Toast.makeText(this, "fill all fields ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!value1.contentEquals(value2)){
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_LONG).show();
            return;
        }

        if (!databaseHelper.checkUser(email)) {

            Toast.makeText(this, "Email doesn't exist", Toast.LENGTH_LONG).show();
            return;

        } else {
            databaseHelper.updatePassword(email, value1);

            Toast.makeText(this, "password reset successfully", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void emptyInputEditText()
    {
        textInputEditTextPassword.setText("");
        textInputEditTextConfirmPassword.setText("");
    }
}


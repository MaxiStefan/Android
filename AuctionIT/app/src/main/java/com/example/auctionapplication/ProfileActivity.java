package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutFName;
    private TextInputLayout textInputLayoutLName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextFName;
    private TextInputEditText textInputEditTextLName;
    private TextInputEditText textInputEditTextEmail;

    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textInputLayoutFName = (TextInputLayout) findViewById(R.id.textInputLayoutFName);
        textInputLayoutLName = (TextInputLayout) findViewById(R.id.textInputLayoutLName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

        textInputEditTextFName = (TextInputEditText) findViewById(R.id.firstName);
        textInputEditTextLName = (TextInputEditText) findViewById(R.id.lastName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.email);

    }
}

package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout textInputLayoutFName;
    private TextInputLayout textInputLayoutLName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextFName;
    private TextInputEditText textInputEditTextLName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));


        textInputLayoutFName = (TextInputLayout) findViewById(R.id.textInputLayoutFName);
        textInputLayoutLName = (TextInputLayout) findViewById(R.id.textInputLayoutLName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextFName = (TextInputEditText) findViewById(R.id.firstName);
        textInputEditTextLName = (TextInputEditText) findViewById(R.id.lastName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.email);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.password);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.confirmPassword);

        Button register = (Button) findViewById(R.id.registerUser);
        register.setOnClickListener(this);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        inputValidation = new InputValidation(RegisterActivity.this);
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerUser:
                postDataToSQLite();
                break;

            case R.id.cancel:
              Intent intent = new Intent(this, LoginActivity.class);
              startActivity(intent);
                break;

        }
    }

        private void postDataToSQLite(){
            if (!inputValidation.isInputEditTextFilled(textInputEditTextFName, textInputLayoutFName, "Enter First Name")) {
                return;
            } if (!inputValidation.isInputEditTextFilled(textInputEditTextLName, textInputLayoutLName, "Enter Last Name")) {
                return;
            }
            if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, "Enter Valid Email")) {
                return;
            }
            if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, "Enter Valid Email")) {
                return;
            }
            if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, "Enter Password")) {
                return;
            }
            if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                    textInputLayoutConfirmPassword, "Password does not match.")) {
                return;
            }

            if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

                user.setFirstName(textInputEditTextFName.getText().toString().trim());
                user.setLastName(textInputEditTextLName.getText().toString().trim());
                user.setEmail(textInputEditTextEmail.getText().toString().trim());
                user.setPassword(textInputEditTextPassword.getText().toString().trim());

                databaseHelper.addUser(user);

                // show success message that record saved successfully
                Toast.makeText(getApplicationContext(), "Success Register", Toast.LENGTH_LONG).show();
                emptyInputEditText();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


            } else {
                // show error message that record already exists
                Toast.makeText(getApplicationContext(),"Email Already Exists", Toast.LENGTH_LONG).show();
            }


        }
    private void emptyInputEditText(){
        textInputEditTextFName.setText(null);
        textInputEditTextLName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}

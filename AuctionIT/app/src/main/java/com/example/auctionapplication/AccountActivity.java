package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

        Button signIn = (Button)findViewById(R.id.signIN);
        signIn.setOnClickListener(this);
        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.search:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.auctions:
                intent = new Intent(this, AuctionsActivity.class);
                break;
            case R.id.account:
                intent = new Intent(this, AccountActivity.class);
                break;
            case R.id.signIN:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }
}

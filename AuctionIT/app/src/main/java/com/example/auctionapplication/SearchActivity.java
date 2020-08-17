package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, GridView.OnItemClickListener {

    String[] labels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Resources res = getResources();
        labels = res.getStringArray(R.array.headings);

        GridView grid = (GridView) findViewById( R.id.smthGrid );
        CustomAdapter myAdapter = new CustomAdapter( getApplicationContext(), labels );
        grid.setAdapter( myAdapter );

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);
        grid.setOnItemClickListener(this);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

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
        }
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
           intent.putExtra("name",labels[position]);
        startActivity(intent);
    }
}

package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    URL imageURL = null;
    InputStream is = null;
    Bitmap bmIMG = null;
    ImageView imgView = null;
    ProgressDialog p ;
    private DatabaseHelper databaseHelper;
    public String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_display);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

        TextView userHello = (TextView) findViewById(R.id.user);

        Intent intent = getIntent();
        if (intent.hasExtra("EMAIL")){
            String nameFromIntent = getIntent().getStringExtra("EMAIL");
            email = nameFromIntent;
            userHello.setText("Welcome " + nameFromIntent);
        }else{
            String email = PreferenceUtils.getEmail(this);
            userHello.setText("Welcome " + email);

        }

        imgView = (ImageView)findViewById(R.id.randomImage);
        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute("https://i.redd.it/k9xjxdm3m0k31.jpg");

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);
        Button logOut = (Button) findViewById(R.id.LogOut);
        logOut.setOnClickListener(this);
        Button deleteMe = (Button) findViewById(R.id.deleteUser);
        deleteMe.setOnClickListener(this);
        Button profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(LoginDisplayActivity.this);
    }

    public class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(LoginDisplayActivity.this);
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                imageURL = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmIMG = BitmapFactory.decodeStream(is, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmIMG;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(imgView!=null) {
                p.hide();
                imgView.setImageBitmap(bitmap);
            }else {
                p.show();
            }
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.auctions:
                intent = new Intent(this, AuctionsActivity.class);
                startActivity(intent);
                break;
            case R.id.account:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.LogOut:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.deleteUser:
                databaseHelper.deleteUser(email);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }

    }

}

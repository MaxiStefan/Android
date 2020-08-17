package com.example.auctionapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,RecyclerAdapter.OnItemClickListener {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private RecyclerAdapter recAdapter;
    private List<Art> artList;
    private List<Art> filteredList;

    public static final String Extra_URL ="imageURL";
    public static final String Extra_BYTE ="imageBYTE";
    public static final String Extra_Name ="name";
    public static final String Extra_Date ="date";
    public static final String Extra_Value ="estimated-value";
    public static final String Extra_Bid ="starting-bid";
    public static final int ROOM = 0;
    public static final int MAPS = 1;
    public static final int GRAPHICS = 2;
    public static final int FIRESTORE = 3;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //want to lay items by default linearly
        artList = new ArrayList<>();
        filteredList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        final ArtDB artsDB = ArtDB.getInstance(MainActivity.this);

        artList = artsDB.getArtDao().getAll();


        if (intent.hasExtra("name")) {
            String artTypeFilter = getIntent().getStringExtra("name");
            parseFiltered(artTypeFilter);
        }else{
            jsonParse();
        }

    }

    private void parseFiltered(String type){
         final ArtDB artsDB = ArtDB.getInstance(MainActivity.this);
        filteredList = artsDB.getArtDao().getByArtType(type);
        recAdapter = new RecyclerAdapter(MainActivity.this,filteredList);
        recyclerView.setAdapter(recAdapter);
        recAdapter.setOnItemClickListener(MainActivity.this);

    }
    private void jsonParse(){
        String url = "https://pastebin.com/raw/g9jGgW4d";
        final ArtDB artsDB = ArtDB.getInstance(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    //on Success
                        try {
                            JSONArray jsonArray = response.getJSONArray("Art");
                            Log.w("hello",jsonArray.toString());
                            for (int i =0; i < jsonArray.length(); i++){
                                JSONObject art = jsonArray.getJSONObject(i);
                                Log.w("hello",art.toString());

                                String type = art.getString("type");
                                String date = art.getString("date");
                                String name = art.getString("name");
                                Double estvalue = art.getDouble("estimated-value");
                                Double startingBid = art.getDouble("starting-bid");
                                String imgURL = art.getString("imageURL");

                                int count = artsDB.getArtDao().getCount(name);
                                if(count == 0){
                                    artList.add(new Art(type,date,name,estvalue,startingBid,imgURL));
                                    artsDB.getArtDao().insert(new Art(type,date,name,estvalue,startingBid,imgURL));
                                }
                            }
                                recAdapter = new RecyclerAdapter(MainActivity.this,artList);
                                recyclerView.setAdapter(recAdapter);
                                recAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on Error
                error.printStackTrace();
            }
        });
        mQueue.add(request);
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
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailsActivity.class);
        Art clickedArt = artList.get(position);
        detailIntent.putExtra(Extra_Name,clickedArt.getName());
        detailIntent.putExtra(Extra_Bid,clickedArt.getStartingBid());
        detailIntent.putExtra(Extra_Date,clickedArt.getDate());
        if(clickedArt.getImageByte() != null){
            detailIntent.putExtra(Extra_BYTE,clickedArt.getImageByte());
        }else if (clickedArt.getImageURL() != null){
            detailIntent.putExtra(Extra_URL,clickedArt.getImageURL());
        }
        detailIntent.putExtra(Extra_Value,clickedArt.getEstimatedValue());
        startActivity(detailIntent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,ROOM,0, "ROOMDB");
        menu.add(0,MAPS,1, "Maps");
        menu.add(0,GRAPHICS,2, "Graphics");
        menu.add(0,FIRESTORE,3, "Firestore");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case ROOM:
                Intent intent=new Intent(this, RoomActivity.class);
                startActivity(intent);
                break;
            case MAPS:
                Intent intent1 = new Intent(this, MapsActivity.class);
                startActivity(intent1);
                break;
            case GRAPHICS:
                Intent intent2 = new Intent(this, GraphicsActivity.class);
                startActivity(intent2);
                break;
            case FIRESTORE:
                Intent intent3 = new Intent(this, FirebaseActivity.class);
                startActivity(intent3);
        }
        return true;
    }
}



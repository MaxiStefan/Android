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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

public class FirebaseActivity extends AppCompatActivity implements View.OnClickListener,RecyclerAdapter.OnItemClickListener {
    private TextView mTextViewResult;
    private RecyclerView recyclerView;
    private RecyclerAdapter recAdapter;
    private List<Art> artList;

    public static final String Extra_URL ="imageURL";
    public static final String Extra_BYTE ="imageBYTE";
    public static final String Extra_Name ="name";
    public static final String Extra_Date ="date";
    public static final String Extra_Value ="estimated-value";
    public static final String Extra_Bid ="starting-bid";
    public static final int ROOM = 0;
    public static final int MAPS = 1;
    public static final int GRAPHICS = 2;


    private static final String TAG = "AuctionsActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ESTIMATEDVALUE = "value";
    private static final String KEY_STARTINGBID = "bid";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE = "image";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference artRef = db.collection("Arts").document("My First art");


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
        loadArt();


    }


    public void loadArt(){
        artRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString(KEY_TITLE);
                            String date = documentSnapshot.getString(KEY_DATE);
                            Double estvalue = documentSnapshot.getDouble(KEY_ESTIMATEDVALUE);
                            Double startingBid = documentSnapshot.getDouble(KEY_STARTINGBID);
                            String type = documentSnapshot.getString(KEY_TYPE);

                            artList.add(new Art(type,date,name,estvalue,startingBid));
                            recAdapter = new RecyclerAdapter(FirebaseActivity.this,artList);
                            recyclerView.setAdapter(recAdapter);
                            recAdapter.setOnItemClickListener(FirebaseActivity.this);

                        }else{
                            Toast.makeText(FirebaseActivity.this,"Document does not exist.",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FirebaseActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
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




}



package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Art> artList;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));


        final ArtDB artsDB = ArtDB.getInstance(RoomActivity.this);
        final ListView listView = findViewById(R.id.listView1);
        artList = new ArrayList<>();
        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);

        Button csv = (Button) findViewById(R.id.toCSV);
        csv.setOnClickListener(this);
        Button txt = (Button) findViewById(R.id.toTXT);
        txt.setOnClickListener(this);
        Button getAll = (Button) findViewById(R.id.getAll);
        getAll.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                artList = artsDB.getArtDao().getAll();
                ArrayAdapter<Art> adaptor;
                adaptor = new ArrayAdapter<Art>(v.getContext(), android.R.layout.simple_list_item_1, artsDB.getArtDao().getAll());
                listView.setAdapter(adaptor);
            }

        });
        Button filtered = (Button) findViewById(R.id.getFiltered);
        filtered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artList = artsDB.getArtDao().getByArtYpe();
                ArrayAdapter<Art> adaptor;
                adaptor = new ArrayAdapter<Art>(v.getContext(), android.R.layout.simple_list_item_1, artsDB.getArtDao().getByArtYpe());
                listView.setAdapter(adaptor);
            }

        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                List<Art> newList=artsDB.getArtDao().getAll();
////                Art a=newList.get(position);
////                artsDB.getArtDao().deleteArt(a);
////                ArrayAdapter<Art> adaptor1= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, artsDB.getArtDao().getAll());
////                listView.setAdapter(adaptor1);
                artsDB.getArtDao().deleteAll();
                return true;
            }
        });


    }

    private void writeArtstoFile(String fileName, List<Art> arts)  //throws bc we work with files
    {
        try {
            FileOutputStream file = openFileOutput(fileName, Activity.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(file);
            //serializing the object
            for (Art art : artList) {
                dos.writeInt(art.getId());
                dos.writeUTF(art.getType());
                dos.writeUTF(art.getName());
                dos.writeUTF(art.getDate());
                dos.writeDouble(art.getEstimatedValue());
                dos.writeDouble(art.getStartingBid());
            }
            dos.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(View v) {
        StringBuilder data = new StringBuilder();
        data.append("Type,Date,Name,estimatedValue,startingBid");
        for (Art art : artList) {
            data.append("\n" + art.getType() + "," + art.getDate() + "," + art.getName() + "," + art.getEstimatedValue() + "," + art.getStartingBid());
        }

        try {

            //saving file into deice
            FileOutputStream file = openFileOutput("data.csv", Activity.MODE_PRIVATE);
            file.write((data.toString()).getBytes());
            file.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toCSV:
                export(v);
                break;
            case R.id.toTXT:
                writeArtstoFile("art.txt", artList);
                break;
            case R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.auctions:
                Intent intent2 = new Intent(this, AuctionsActivity.class);
                startActivity(intent2);
                break;
            case R.id.account:
                Intent intent3 = new Intent(this, AccountActivity.class);
                startActivity(intent3);
                break;
        }
    }


}


package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.auctionapplication.MainActivity.Extra_BYTE;
import static com.example.auctionapplication.MainActivity.Extra_Bid;
import static com.example.auctionapplication.MainActivity.Extra_Date;
import static com.example.auctionapplication.MainActivity.Extra_Name;
import static com.example.auctionapplication.MainActivity.Extra_URL;
import static com.example.auctionapplication.MainActivity.Extra_Value;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String imgURL = intent.getStringExtra(Extra_URL);
        byte[] imgArray = intent.getByteArrayExtra(Extra_BYTE);
        String name = intent.getStringExtra(Extra_Name);
        String date = intent.getStringExtra(Extra_Date);
        Double val = intent.getDoubleExtra(Extra_Value,0);
        Double bid = intent.getDoubleExtra(Extra_Bid, 0);

        ImageView detailImgView = (ImageView)findViewById(R.id.imgView_detail);
        TextView nameView = (TextView)findViewById(R.id.txtName);
        TextView dateView = (TextView)findViewById(R.id.txtDate);
        TextView valView = (TextView)findViewById(R.id.txtValue);
        TextView bidView = (TextView)findViewById(R.id.txtBid);


        if(imgURL != null){
            Picasso.get().load(imgURL).fit().centerInside().into(detailImgView);
        }else if(imgArray != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgArray , 0, imgArray.length);
            detailImgView.setImageBitmap(bitmap);
        }else{
            detailImgView.setImageResource(R.drawable.not_available);

        }
        nameView.setText("Name of art: " + name);
        dateView.setText("Date of listing: " + date);
        valView.setText("Estimated value: " + val.toString());
        bidView.setText("Starting bid: " + bid.toString());

    }
}

package com.example.auctionapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText artTitle;
    private EditText value;
    private EditText bid;
    private EditText imgURL;
    private Spinner itemType;
    private Spinner currency;
    private TextView finalDate;
    private Calendar calendar;
    private DatePickerDialog dpd;
    private ImageView photoView;

    Integer REQUEST_CAMERA = 1, SELECT_FILES = 0;

    String[] currencies = {"EUR", "USD", "GBP"};
    String[] itemTypes = {"Paintings", "Jewelry", "Collectibles", "Furniture", "Firearms"};
    private byte[] imageArray ;

    private static final String TAG = "AuctionsActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ESTIMATEDVALUE = "value";
    private static final String KEY_STARTINGBID = "bid";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE = "image";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

            artTitle = (EditText) findViewById(R.id.artTitle);
            value = (EditText) findViewById(R.id.estimateValue);
            bid = (EditText) findViewById(R.id.startingBid);
            itemType = (Spinner) findViewById(R.id.itemClass);
            currency = (Spinner) findViewById(R.id.currency);
            finalDate = (TextView) findViewById(R.id.finalDate);
            imgURL = (EditText) findViewById(R.id.editTextURL) ;
            photoView = (ImageView) findViewById(R.id.imageViewPhoto);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,currencies);
        currency.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,itemTypes);
        itemType.setAdapter(adapter1);

        Button done = (Button) findViewById(R.id.createObject);
        done.setOnClickListener(this);
        Button loadPhoto = (Button) findViewById(R.id.loadPhoto);
        loadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        Button auctions = (Button) findViewById(R.id.auctions);
        auctions.setOnClickListener(this);
        Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(this);
        Button setDate = (Button) findViewById(R.id.date);
        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AuctionsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        finalDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);
                dpd.show();
            }
        });

        artTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                artTitle.setSelection( artTitle.getText().length(),0);
            }
        });
        bid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bid.setSelection( bid.getText().length(),0);
            }
        });
        imgURL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bid.setSelection( bid.getText().length(),0);
            }
        });

        value.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                value.setSelection( value.getText().length(),0);
            }
        });

    }

    private void selectImage(){

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AuctionsActivity.this);
        builder.setTitle("Load Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i].equals("Camera")){

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }else if (items[i].equals("Gallery")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILES);

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp  = (Bitmap) bundle.get("data");
                photoView.setImageBitmap(bmp);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageArray = baos.toByteArray();

            }else if( requestCode == SELECT_FILES){

                Uri selectedImage =  data.getData();
                photoView.setImageURI(selectedImage);

                Bitmap src =
                        null;
                try {
                     src = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                src.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageArray = baos.toByteArray();

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
            case R.id.createObject:
                String title = artTitle.getText().toString();
                String date = finalDate.getText().toString();
                String type = itemType.getSelectedItem().toString();
                Double estValue = Double.parseDouble(bid.getText().toString());
                Double startingVal = Double.parseDouble(value.getText().toString());
                String imgUrl = imgURL.getText().toString();

//                Art artObject = new Art(type,date,title,estValue,startingVal,imgUrl);
//                intent.putExtra("artObject",artObject);

                final ArtDB artsDB = ArtDB.getInstance(AuctionsActivity.this);
                int count = artsDB.getArtDao().getCount(title);
                if(count == 0){
                    artsDB.getArtDao().insert(new Art(type,date,title,estValue,startingVal,imageArray));
                }

                Map<String,Object> art = new HashMap<>();
                art.put(KEY_TITLE,title);
                art.put(KEY_TYPE,type);
                art.put(KEY_ESTIMATEDVALUE,estValue);
                art.put(KEY_STARTINGBID,startingVal);
                art.put(KEY_DATE,date);
//                List<Byte> imgList = new ArrayList<>();
//                    imgList = convertBytesToList(imageArray);
//                art.put(KEY_IMAGE,imgList);

                db.collection("Arts").document("My First art").set(art)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AuctionsActivity.this,"Art saved",Toast.LENGTH_SHORT).show();
                                }
                            })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AuctionsActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }
    private static List<Byte> convertBytesToList(byte[] bytes) {
        final List<Byte> list = new ArrayList<>();
        for (byte b : bytes) {
            list.add(b);
        }
        return list;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}

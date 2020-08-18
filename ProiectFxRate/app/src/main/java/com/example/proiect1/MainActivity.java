package com.example.proiect1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Main Menu
    public static final int NETWORK=0;
    public static final int CONVERTOR=1;
    public static final int JSON=2;
    public static final int EXIT=3;
    public static final int SAVE=4;
    public static final int ROOM=5;
    public static final int SHAPES=6;
    public static final int SHAPES2=7;
    public static final int MAPS=8;
    EditText USD,EUR,GBP,XAU;

    //firebase
    private DatabaseReference mMessageReference;
    private FirebaseDatabase database;

    TextView tvDate;

    public enum Location{
        //enumerate symbols
        INTERNAL, DATABASE, SDCARD;
        //method of the enum class
        public static Location getLocation(int ordinal){
            return Location.values()[ordinal];
        }
        //another method
        public static String[] getValues(){
            String[] values= new String[Location.values().length];
            int i=0;
            for(Location loc:Location.values()){
                values[i++] = loc.toString();
            }
            return values;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("lifecycle","OnCreate");

        EUR=findViewById(R.id.editTextEur); //if findViewById is called in another method other than onCreate, the object will be NULL
        USD=findViewById(R.id.editTextUSD);
        GBP=findViewById(R.id.editTextGBP);
        XAU=findViewById(R.id.editTextXAU);
        tvDate=findViewById(R.id.fxRateDate);
        //adaptor pt spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Location.getValues());

        final Spinner spinnerLocation=findViewById(R.id.spinnerSave);
        spinnerLocation.setAdapter(adapter);

        Button btn1=(Button)findViewById(R.id.btnRefresh);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Click on refresh button", Toast.LENGTH_LONG).show();
                try{
                    Network n = new Network(){
                        @Override
                        protected void onPostExecute(InputStream inputStream) {
                            tvDate.setText(cv.getDate());
                            EUR.setText(cv.getEUR());
                            USD.setText(cv.getUSD());
                            GBP.setText(cv.getGBP());
                            XAU.setText(cv.getXAU());
                        }
                    };
                    n.execute(new URL("https://www.bnr.ro/nbrfxrates.xml")); // calls do in background method
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }
        });

        FirebaseApp.initializeApp(this);
        database=FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        //Firebase
        mMessageReference = database.getReference("fxrates");
        mMessageReference.keepSynced(true);

        Button btn2=(Button)findViewById(R.id.btnSave);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),"Click on save button",Toast.LENGTH_LONG).show();*/
                //getapplicationcontext bc not in method, if in method we can use "this"

                //to store an FX rate object and retrieve it later
                int id=spinnerLocation.getSelectedItemPosition();
                FXRate cv=new FXRate(id, tvDate.getText().toString(), EUR.getText().toString(),
                        USD.getText().toString(), GBP.getText().toString(), XAU.getText().toString());
                writeNewCVinFirebase(cv);
                try{
                    writeCVtoFile("file.dat", cv);
                    cv=null;
                    cv=readCVfromFile("file.dat");
                    Toast.makeText(getApplicationContext(), cv.toString(), Toast.LENGTH_LONG).show();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


                SharedPreferences settingsFile = getSharedPreferences("CVPrefs", 0);
                SharedPreferences.Editor myEditor = settingsFile.edit();
                myEditor.putInt("id", cv.getId());
                myEditor.putString("data", cv.getDate());
                myEditor.putString("EUR", cv.getEUR());
                myEditor.putString("USD", cv.getUSD());
                myEditor.putString("GBP", cv.getGBP());
                myEditor.putString("XAU", cv.getXAU());
                myEditor.apply(); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });
    }

    private void writeNewCVinFirebase(final FXRate cv){
        final DatabaseReference myRef=database.getReference("fxrates");
        myRef.keepSynced(true);

        myRef.child("fxrates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cv.setUid(myRef.child("fxrates").push().getKey());
                myRef.child("fxrates").child(cv.getUid()).setValue(cv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void writeCVtoFile(String fileName, FXRate cv) throws IOException //throws bc we work with files
    {
        FileOutputStream file = openFileOutput(fileName, Activity.MODE_PRIVATE);
        DataOutputStream dos = new DataOutputStream(file);
        //serializing the object
        dos.writeInt(cv.getId());
        dos.writeUTF(cv.getDate());
        dos.writeUTF(cv.getEUR());
        dos.writeUTF(cv.getUSD());
        dos.writeUTF(cv.getGBP());
        dos.writeUTF(cv.getXAU());
        dos.flush();
        file.close();
    }

    private FXRate readCVfromFile (String fileName) throws IOException{
        FileInputStream file = openFileInput(fileName);
        DataInputStream dis=new DataInputStream(file);
        FXRate cv = new FXRate();
        //deserializing
        cv.setId(dis.readInt());
        cv.setDate(dis.readUTF());
        cv.setEUR(dis.readUTF());
        cv.setUSD(dis.readUTF());
        cv.setGBP(dis.readUTF());
        cv.setXAU(dis.readUTF());

        file.close();
        return cv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,NETWORK,0,"EXTRACT FX RATE");
        menu.add(0,CONVERTOR,1,"CONVERTOR");
        menu.add(0,JSON,2,"JSON");
        menu.add(0,EXIT,3,"EXIT");
        menu.add(0,SAVE,4,"SAVE SHARED");
        menu.add(0,ROOM,5,"ROOMDB");
        menu.add(0,SHAPES,6,"SHAPES");
        menu.add(0,SHAPES2,7,"SHAPES2");
        menu.add(0,MAPS,8,"MAPS");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case NETWORK:
            {
                Network n=new Network(){
                @Override
                        protected void onPostExecute(InputStream inputStream) {
                    List<Currency> currencies=parseXML(inputStream);
                    for(int i=0;i<currencies.size();i++){
                        Log.v("currencies-XML", currencies.get(i).toString());
                    }
            }};
                try{
                    n.execute(new URL("https://www.bnr.ro/nbrfxrates.xml"));
                }catch(MalformedURLException e)
                {
                    e.printStackTrace();
                }


                JsonRead jsonRead = new JsonRead(){
                    @Override
                    protected void onPostExecute(String s) {
                        List<Currency> currencies = parseJson(s);
                        for(int i = 0; i< currencies.size(); i++) {
                            Log.v("currency_JSON", currencies.get(i).toString());
                        }
                    }
                };
                try {
                    jsonRead.execute(new URL("https://api.exchangeratesapi.io/latest"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

//                String rates=n.extractFXRates();
//                if(rates!=null)
//                {
//                    Toast.makeText(getApplicationContext(),rates, Toast.LENGTH_LONG).show();
//                    //parsing the xml file!!!!!!!!
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Internet error", Toast.LENGTH_LONG).show();
//                }
                break;
            }
            case CONVERTOR:
            {
//                EditText EUR=findViewById(R.id.editTextEur);
//                EditText USD=findViewById(R.id.editTextUSD);
//                EditText GBP=findViewById(R.id.editTextGBP);


                Intent intent1=new Intent(getApplicationContext(),ConvertorActivity.class);
                intent1.putExtra("FXrateEUR", EUR.getText().toString());
                intent1.putExtra("FXrateUSD",USD.getText().toString());
                intent1.putExtra("FXrateGBP", GBP.getText().toString());
                startActivity(intent1);
                break;
            }

            case JSON:
            {
                Intent intent2=new Intent(this, JSONActivity.class);
                startActivity(intent2);
                break;
            }
            case EXIT:
            {
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            }
            case SAVE:{
                SharedPreferences settingsFile=getSharedPreferences("CVPrefs", 0);
                int id=settingsFile.getInt("id",0); //second param is the default value
                String data=settingsFile.getString("data", null);
                String EUR=settingsFile.getString("EUR", null);
                String USD=settingsFile.getString("USD", null);
                String GBP=settingsFile.getString("GBP", null);
                String XAU=settingsFile.getString("XAU", null);
                FXRate cv=new FXRate(id,data, EUR, GBP, USD, XAU);
                Toast.makeText(this, cv.toString(), Toast.LENGTH_LONG).show();
                break;
            }

            case ROOM:
                Intent intent3=new Intent(this, RoomActivity.class);
                startActivity(intent3);
                break;
            case SHAPES:
                Intent intent4=new Intent(this, ActivityShapes.class);
                startActivity(intent4);
            case SHAPES2:
                Intent intent5=new Intent(this, ActivityGraphic.class);
                startActivity(intent5);
            case MAPS:
                Intent intent6=new Intent(this, MapsActivity.class);
                startActivity(intent6);
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle","OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle","OnResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle","OnRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle","OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lifecycle","OnDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle","OnPause");
    }


}


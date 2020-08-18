package com.example.proiect1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        FXRate curs1=new FXRate(100, "2019-02-12", "4.79", "4.34", "5.31", "203,12");
        FXRate curs2=new FXRate(101, "2019-05-12", "4.77", "4.21", "5.10", "212,19");
        FXRate curs3=new FXRate(102, "2019-11-12", "4.29", "4.14", "5.61", "217,12");

        final List<FXRate> cursuri = Arrays.asList(curs1,curs2,curs3);
        final FXRatesDB cursuriDB=FXRatesDB.getInstance(this);
        cursuriDB.getDaoRate().deleteAll();
        cursuriDB.getDaoRate().insert(cursuri);

        final ListView listView=findViewById(R.id.listView1);

        ArrayAdapter<FXRate> adaptor= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cursuriDB.getDaoRate().getAll());

        listView.setAdapter(adaptor);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                List<FXRate> newList=cursuriDB.getDaoRate().getAll();
                FXRate cv=newList.get(position);
                cursuriDB.getDaoRate().deleteCV(cv);
                ArrayAdapter<FXRate> adaptor1= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, cursuriDB.getDaoRate().getAll());
                listView.setAdapter(adaptor1);
                return true;
            }
        });
    }
}

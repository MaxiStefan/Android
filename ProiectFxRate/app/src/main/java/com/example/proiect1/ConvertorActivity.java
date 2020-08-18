package com.example.proiect1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
//for the OnClick method we need to implement View.OnClickListener, then it will automatically generate the method
public class ConvertorActivity extends AppCompatActivity implements View.OnClickListener {

//before on create because the controls need to be available also in other methods
// if they are defined in the oncreate, they won't be accessible for other methods in the class

    private EditText amount1;
    private EditText amount2;
    private Spinner spinner1;
    private Spinner spinner2;
    //pentru varianta din layout dialog
    Dialog dlg=null;
    //source of elements for spinner, to link it to the spinner we need to define an adapter
    String[] array={"EUR","USD","GBP","RON"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertor);
        amount1=new EditText(this); //autocomplete
        amount2=new EditText(this);
        spinner1=new Spinner(this);
        spinner2=new Spinner(this);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_dropdown_item,array);
        //simple spinner dropdown item is a predefined layout
        //linking the source to the spinners
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        //defining a text view, kind of a label
        TextView tv=new TextView(this);
        tv.setText("FX convertor");

        Button btn1=new Button(this);
        btn1.setText("Calculate");
        btn1.setOnClickListener(this);


        LinearLayout ll=findViewById(R.id.ll);
        //important order, exactly how we want them to be displayed
        ll.addView(tv);
        ll.addView(amount1);
        ll.addView(spinner1);
        ll.addView(amount2);
        ll.addView(spinner2);
        ll.addView(btn1);
    }

    @Override
    public void onClick(View v) {
        Intent intent1=getIntent();
        String EURrate=intent1.getExtras().getString("FXrateEUR");
        String USDrate=intent1.getExtras().getString("FXrateUSD");
        String GBPrate=intent1.getExtras().getString("FXrateGBP");
        //varianta cod
        /*final Dialog dlg = new Dialog(this);
        LinearLayout linl=new LinearLayout(this);
        linl.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.eur); //icon
        TextView tv = new TextView(this);
        tv.setText("Conversion done!");
        Button btn = new Button(this);
        btn.setText("OK");
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dlg.dismiss();
            }
        });
        linl.addView(iv);
        linl.addView(tv);
        linl.addView(btn);
        dlg.setContentView(linl); //so the layout appears in the dialog
*/
        //varianta layout
        dlg = new Dialog(this);
        dlg.setContentView(R.layout.dialog);

        double val1=0.00; //source value, not mandatory to be double
        double val2=0.00; //calculated value

        try{
            val1=Double.parseDouble(amount1.getText().toString());
        } catch (Exception e) {
            Log.e("Convertor",e.getMessage());
            Toast.makeText(this,"Insert a numerical value!"+ e.getMessage(),Toast.LENGTH_LONG).show();
        }


        //checking what values were selected


        switch (spinner1.getSelectedItem().toString()){
            case "EUR":
                switch(spinner2.getSelectedItem().toString()){
                    case "RON":{
                        val2=val1*Double.parseDouble(EURrate);
                        amount2.setText(Double.toString(val2));
                        dlg.show(); break;
                    }
                    case "GBP":{
                        val2=(val1*Double.parseDouble(EURrate))/Double.parseDouble(GBPrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                    case "USD":{
                        val2=(val1*Double.parseDouble(EURrate))/Double.parseDouble(USDrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                    case "EUR":{
                        Toast.makeText(this,"Please select a different currency.",Toast.LENGTH_LONG).show();
                        amount2.setText("");
                        break;
                    }
                } break;
            case "GBP":
                switch(spinner2.getSelectedItem().toString()){
                    case "RON":{
                        val2=val1*Double.parseDouble(GBPrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                    case "GBP":{
                        Toast.makeText(this,"Please select a different currency.",Toast.LENGTH_LONG).show();
                        amount2.setText("");
                        break;
                    }
                    case "USD":{
                        val2=(val1*Double.parseDouble(GBPrate))/Double.parseDouble(USDrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                    case "EUR":{
                        val2=(val1*Double.parseDouble(GBPrate))/Double.parseDouble(EURrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                } break;
            case "USD":
                switch(spinner2.getSelectedItem().toString()){
                    case "RON":{
                        val2=val1*Double.parseDouble(USDrate);
                        amount2.setText(Double.toString(val2));dlg.show(); break;
                    }
                    case "USD":{
                        Toast.makeText(this,"Please select a different currency.",Toast.LENGTH_LONG).show();
                        amount2.setText("");
                        break;
                    }
                    case "GBP":{
                        val2=(val1*Double.parseDouble(USDrate))/Double.parseDouble(GBPrate);
                        amount2.setText(Double.toString(val2)); dlg.show();break;
                    }
                    case "EUR":{
                        val2=(val1*Double.parseDouble(USDrate))/Double.parseDouble(EURrate);
                        amount2.setText(Double.toString(val2)); dlg.show(); break;
                    }
                } break;
            case "RON":
                switch(spinner2.getSelectedItem().toString()){
                    case "RON":{
                        Toast.makeText(this,"Please select a different currency.",Toast.LENGTH_LONG).show();
                        amount2.setText("");
                        break;
                    }
                    case "GBP":{
                        val2=val1/Double.parseDouble(GBPrate);
                        amount2.setText(Double.toString(val2)); dlg.show();break;
                    }
                    case "EUR":{
                        val2=val1/Double.parseDouble(EURrate);
                        amount2.setText(Double.toString(val2));dlg.show(); break;
                    }
                    case "USD":{
                        val2=val1/Double.parseDouble(USDrate);
                        amount2.setText(Double.toString(val2)); dlg.show();break;
                    }
                } break;
        }

        Fragment1 fragment1=new Fragment1();
        fragment1.changeTextView();
    }

    public void onClick1(View v) {
        dlg.dismiss();
    }
}

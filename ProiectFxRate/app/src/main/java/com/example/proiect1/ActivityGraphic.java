package com.example.proiect1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.series.XYSeries;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XLayoutStyle;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.xy.YLayoutStyle;

import java.util.Arrays;
import java.util.List;

public class ActivityGraphic extends AppCompatActivity {

    private XYPlot plot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        TextView tvTitle = new TextView(this);
        tvTitle.setText("Grafic evolutie curs");
        tvTitle.setGravity(Gravity.CENTER);

        ll.addView(tvTitle);

        List<Double> lstEUR = Arrays.asList(Double.valueOf(140.77), Double.valueOf(100.88), Double.valueOf(150.55));
        List<Double> lstUSD = Arrays.asList(Double.valueOf(50.88), Double.valueOf(80.55), Double.valueOf(130.99));
        List<Double> lstGBP = Arrays.asList(Double.valueOf(120.77), Double.valueOf(14.22), Double.valueOf(150.44));

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

        // add a new series
        XYSeries seriesEUR = new SimpleXYSeries(lstEUR,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "EUR");
        plot.addSeries(seriesEUR, new LineAndPointFormatter(
                getApplicationContext(), R.layout.f1));

        // add a new series
        XYSeries seriesUSD = new SimpleXYSeries(lstUSD,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "USD");
        plot.addSeries(seriesUSD, new LineAndPointFormatter(
                getApplicationContext(), R.layout.f2));

        // add a new series
        XYSeries seriesGBP = new SimpleXYSeries(lstGBP,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "GBP");
        plot.addSeries(seriesGBP, new LineAndPointFormatter(
                getApplicationContext(), R.layout.f3));

        // reposition the domain label to look a little cleaner:
        plot.position(plot.getDomainLabelWidget(), // the widget to position
                45,                                // x position value, in this case 45 pixels
                XLayoutStyle.ABSOLUTE_FROM_LEFT,   // how the x position value is applied, in this case from the left
                0,                                 // y position value
                YLayoutStyle.ABSOLUTE_FROM_BOTTOM, // how the y position is applied, in this case from the bottom
                AnchorPosition.LEFT_BOTTOM);       // point to use as the origin of the widget being positioned

        plot.setTitle("Grafic evolutie curs");
        plot.centerOnRangeOrigin(60);//60
        plot.centerOnDomainOrigin(5);//5
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 0.5);
        plot.getLayoutManager().remove(plot.getDomainLabelWidget());
        plot.getLayoutManager().remove(plot.getRangeLabelWidget());

        //inregistrare meniu contextual
        registerForContextMenu(plot);
    }
}

package com.example.auctionapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphicsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    final ArtDB artsDB = ArtDB.getInstance(GraphicsActivity.this);
    int paintings = artsDB.getArtDao().getCountAllPaintings();
    int jewelry = artsDB.getArtDao().getCountAllJews();
    int furniture = artsDB.getArtDao().getCountAllFurniture();
    int firearms = artsDB.getArtDao().getCountAllFirearms();
    int collectibles = artsDB.getArtDao().getCountAllCollectibles();
    float values[] = {paintings, jewelry, firearms, furniture, collectibles};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        TextView title = (TextView) findViewById(R.id.graphTitle);
        TextView paintCol = (TextView) findViewById(R.id.paintCol);
        TextView furCol = (TextView) findViewById(R.id.furnitCol);
        TextView fireCol = (TextView) findViewById(R.id.firearmsCol);
        TextView jewCol = (TextView) findViewById(R.id.jewelryCol);
        TextView collCol = (TextView) findViewById(R.id.collectibleCol);
        title.setText("Art items type variance.");
        paintCol.setText("Paintings - Blue.");
        furCol.setText("Furniture - Green.");
        fireCol.setText("FireArms - Gray.");
        jewCol.setText("Jewelry - Cyan.");
        collCol.setText("Collectible - Red.");

        LinearLayout linear = findViewById(R.id.linear);
        values = calculateData(values);
        linear.addView(new MyGraphicsView(this, values));

    }

    private float[] calculateData(float[] data) {
        // TODO Auto-generated method stub
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 360 * (data[i] / total);
        }
        return data;

    }
}

package com.example.auctionapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private Integer[] images = {
            R.drawable.paintings,
            R.drawable.jewelry,
            R.drawable.collectibles,
            R.drawable.furniture,
            R.drawable.firearms
    };
    //android turns images into IDs that's why we use Integer and not String
    private String[] imageLabels;
    private LayoutInflater thisInflater;

    public CustomAdapter(Context con, String[] labs) {
        this.thisInflater = LayoutInflater.from(con);
        this.imageLabels = labs;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null){
            convertView = thisInflater.inflate( R.layout.grid_item, parent, false );

            TextView textHeading = (TextView) convertView.findViewById(R.id.paintHeading);
            ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.paintImage);

            textHeading.setText( imageLabels[position] );
            thumbnailImage.setImageResource( images[position] );
        }
        return convertView;
    }
}

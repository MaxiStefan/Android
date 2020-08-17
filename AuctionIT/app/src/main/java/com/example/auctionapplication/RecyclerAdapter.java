package com.example.auctionapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private Context con;
    private List<Art> artList;
    private OnItemClickListener listener;

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public RecyclerAdapter (Context con, List<Art> art){
        this.con = con;
        this.artList = art;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.cardview_item, parent, false);
        //always like this
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Art currentArt = artList.get(position);

        String name = currentArt.getName();
        Double value = currentArt.getEstimatedValue();
        holder.txtViewArt.setText(name);
        holder.txtViewVal.setText("Estimated Value: " + String.valueOf(value));
        if(currentArt.getImageURL() != null){
            String imageURL = currentArt.getImageURL();
            Picasso.get().load(imageURL).fit().centerInside().into(holder.imgView);
        }else if(currentArt.getImageByte() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(currentArt.getImageByte() , 0, currentArt.getImageByte().length);
            holder.imgView.setImageBitmap(bitmap);
        }else{
            holder.imgView.setImageResource(R.drawable.not_available);

        }



    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
            public ImageView imgView;
            public TextView txtViewArt;
            public TextView txtViewVal;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.card_imgView);
            txtViewArt = itemView.findViewById(R.id.art_txtView);
            txtViewVal = itemView.findViewById(R.id.value_txtView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener != null){
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION){
                            //valid position
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

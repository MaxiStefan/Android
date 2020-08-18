package com.example.proiect1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONActivity extends ListActivity { //so the activity is seen as a list activity
    private ProgressDialog pDialog;
    //JSON Node names
    private static final String TAG_MOVIES="movies";

    //JSON attributes
    private static final String TAG_ID="id";
    private static final String TAG_TITLE="title";
    private static final String TAG_DURATION="duration";
    private static final String TAG_RELEASE="release";

    //JSON Array
    JSONArray movies=null;

    //colection to store movies
    ArrayList<HashMap<String, String>> movieList; //keys and values


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        movieList = new ArrayList<HashMap<String, String>>();

        URL url=null;
        try{
            url=new URL("http://movio.biblacad.ro/movies.json");
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        GetMovies m=new GetMovies();
        m.setOnTaskFinishedEvent(new OnTaskExecutionFinished() {
            @Override
            public void OnTaskFinishedEvent(String result) {
                if(pDialog.isShowing()){
                    try{
                        Thread.sleep(3000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    pDialog.dismiss();
                }

                ListAdapter adapter = new SimpleAdapter(
                        JSONActivity.this,
                        movieList,
                        R.layout.list_item,
                        new String[] {TAG_TITLE, TAG_RELEASE, TAG_DURATION},
                        new int[] {R.id.title, R.id.release, R.id.duration}
                )
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view= super.getView(position, convertView, parent);
                        HashMap<String, String> currentRow=movieList.get(position);

                        TextView duration=(TextView) view.findViewById(R.id.duration);
                        int valDuration=Integer.parseInt(currentRow.get(TAG_DURATION));
                        if(valDuration<100) duration.setTextColor(Color.RED);
                        else duration.setTextColor(Color.GREEN);

                        return view;
                   }
                };

                setListAdapter(adapter);
            }
        });
        m.execute(url); //important! doInBackground

    }

    public interface OnTaskExecutionFinished{
        void OnTaskFinishedEvent (String result);
    }

    public class GetMovies extends AsyncTask<URL, Void, String>{
        private OnTaskExecutionFinished event;

        public void setOnTaskFinishedEvent (OnTaskExecutionFinished _event){
            if(_event!=null){
                this.event=_event;
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) urls[0].openConnection();
                conn.setRequestMethod("GET"); //let the connection know that we want to get something from that address
                InputStream ist = conn.getInputStream();
                InputStreamReader isr=new InputStreamReader(ist);
                BufferedReader reader=new BufferedReader(isr);
                String buffer="";
                String line="";
                while((line=reader.readLine())!=null)
                {
                    buffer+=line;
                }
                loadJSONObject(buffer); //important!!
                return buffer;

                } catch (Exception ex) {
                    Log.e("doInBackground", ex.getMessage());
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            return null; //if it reaches the return, something is not ok :(
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog=new ProgressDialog(JSONActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false); //skippable
            pDialog.show(); //not skippable
        }

        @Override
        protected void onPostExecute(String s) {
            if(this.event!=null){
                this.event.OnTaskFinishedEvent(s); //method from interface
            } else{
                Log.d("GetMovies","task finished event is null");
            }
        }

        public void loadJSONObject(String jsonStr){
            if(jsonStr!=null){
                try{
                    JSONObject jsonObject = null;
                    try{
                        jsonObject = new JSONObject(jsonStr);
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                    movies = jsonObject.getJSONArray(TAG_MOVIES);

                    for(int i=0;i<movies.length();i++){
                        JSONObject c=movies.getJSONObject(i);
                        String id=c.getString(TAG_ID);
                        String title=c.getString(TAG_TITLE);
                        String duration=c.getString(TAG_DURATION);
                        String release=c.getString(TAG_RELEASE);

                        HashMap<String,String> movie=new HashMap<String,String>();
                        movie.put(TAG_ID, id);
                        movie.put(TAG_TITLE,title);
                        movie.put(TAG_DURATION,duration);
                        movie.put(TAG_RELEASE,release);

                        movieList.add(movie);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else{
                Log.e("loadJSONObject", "Couldn't get any data from the string");
            }
        }
    }
}

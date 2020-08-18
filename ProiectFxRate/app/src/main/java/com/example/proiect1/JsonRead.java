package com.example.proiect1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonRead extends AsyncTask<URL, Void, String> {
    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection conn = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            InputStream ist = conn.getInputStream();

            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader reader = new BufferedReader(isr);
            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception ex) {
            Log.e("doInBackground", ex.getMessage());
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return stringBuilder.toString();
    }

    public List<Currency> parseJson(String json) {
        List<Currency> currencies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject ratesObject = jsonObject.getJSONObject("rates");
            Iterator<String> keys =  ratesObject.keys();
            while (keys.hasNext()) {
                String currency = keys.next();
                Double currencyValue = ratesObject.getDouble(currency);
                Currency currency1 = new Currency(currency, currencyValue);
                currencies.add(currency1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currencies;
    }
}

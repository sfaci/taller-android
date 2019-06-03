package com.sfaci.contacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sfaci.contacts.adapters.EventAdapter;
import com.sfaci.contacts.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity que lista unos eventos obtenidos del catálogo de datos abiertos de Madrid
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class EventsActivity extends Activity implements AdapterView.OnItemClickListener {

    private List<Event> events;
    private EventAdapter adapter;
    private final String EVENTS_URL = "https://datos.madrid.es/portal/site/egob/menuitem.ac61933d6ee3c31cae77ae7784f1a5a0/?vgnextoid=00149033f2201410VgnVCM100000171f5a0aRCRD&format=json&file=0&filename=206974-0-agenda-eventos-culturales-100&mgmtid=6c0b6d01df986410VgnVCM2000000c205a0aRCRD&preview=full";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        events = new ArrayList<>();
        ListView lvEvents = findViewById(R.id.lvEvents);
        adapter = new EventAdapter(this,
                R.layout.event_item, events);
        lvEvents.setAdapter(adapter);
        lvEvents.setOnItemClickListener(this);

        DownloadData descarga = new DownloadData();
        descarga.execute(EVENTS_URL);
    }

    @Override
    protected void  onResume() {
        super.onResume();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Event event = events.get(position);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("name", event.getName());
        intent.putExtra("latitude", event.getLatitude());
        intent.putExtra("longitude", event.getLongitude());
        startActivity(intent);
    }

    private class DownloadData extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;
        private String result;

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)
                        url.openConnection();
                // Lee el fichero de datos y genera una cadena de texto como result
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null)
                    sb.append(line + "\n");

                connection.disconnect();
                br.close();
                result = sb.toString();
                System.out.println(result);

                JSONObject json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("@graph");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Event event = new Event();
                        event.setName(jsonArray.getJSONObject(i).getString("title"));
                        event.setDescription(jsonArray.getJSONObject(i).getString("description"));
                        event.setAddress(jsonArray.getJSONObject(i).getString("event-location"));
                        event.setPrice(Float.parseFloat(jsonArray.getJSONObject(i).getString("price")));
                        event.setDate(new Date());
                        event.setLatitude(jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"));
                        event.setLongitude(jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"));
                        events.add(event);

                        System.out.println(event.getName());
                    } catch (JSONException jsone) {
                        jsone.printStackTrace();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(EventsActivity.this);
            dialog.setTitle(R.string.msg_loading_data);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog != null)
                dialog.dismiss();

            adapter.notifyDataSetChanged();
        }
    }



}

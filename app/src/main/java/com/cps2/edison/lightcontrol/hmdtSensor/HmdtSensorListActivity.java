package com.cps2.edison.lightcontrol.hmdtSensor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.cps2.edison.lightcontrol.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HmdtSensorListActivity extends AppCompatActivity {

    private ListView hmdtSensorListView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private HmdtSensorHttpController controller;
    private HmdtSensorWebsocketsController websocketsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hmdt_sensor_list);
        hmdtSensorListView = (ListView) findViewById(R.id.hmdtSensorListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        hmdtSensorListView.setAdapter(adapter);
        adapter.add("Loading...");
        adapter.notifyDataSetChanged();
        this.controller = new HmdtSensorHttpController(Volley.newRequestQueue(this), HmdtSensorListActivity.this);
        this.controller.retrieveHmdtSensorList();
        this.websocketsController = new HmdtSensorWebsocketsController(HmdtSensorListActivity.this);
    }

    @Override
    protected void onResume() {
        this.websocketsController.connect();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.websocketsController.disconnect();
        super.onPause();
    }

    public void onUpdate(String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray hmdtSensors = new JSONArray(response);
                    adapter.clear();
                    for (int i = 0; i < hmdtSensors.length(); i++) {
                        JSONObject sensor = hmdtSensors.getJSONObject(i);
                        adapter.add(sensor.getString("id") + " | Relative Humidity: " + sensor.getString("humidity"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(HmdtSensorListActivity.this, "Error loading humidity sensor list", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

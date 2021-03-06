package com.cps2.edison.lightcontrol.tempSensor;

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

public class TempSensorListActivity extends AppCompatActivity {

    private ListView tempSensorListView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private TempSensorHttpController controller;
    private TempSensorWebsocketsController websocketsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_sensor_list);
        tempSensorListView = (ListView) findViewById(R.id.tempSensorListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        tempSensorListView.setAdapter(adapter);
        adapter.add("Loading...");
        adapter.notifyDataSetChanged();
        this.controller = new TempSensorHttpController(Volley.newRequestQueue(this), TempSensorListActivity.this);
        this.controller.retrieveTempSensorList();
        this.websocketsController = new TempSensorWebsocketsController(TempSensorListActivity.this);
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
                    JSONArray tempSensors = new JSONArray(response);
                    adapter.clear();
                    for (int i = 0; i < tempSensors.length(); i++) {
                        JSONObject sensor = tempSensors.getJSONObject(i);
                        adapter.add(sensor.getString("id") + " | Temperature: " + sensor.getString("temperature"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(TempSensorListActivity.this, "Error loading temperature sensor list", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

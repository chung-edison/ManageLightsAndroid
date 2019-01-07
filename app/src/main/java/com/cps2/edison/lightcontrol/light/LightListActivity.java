package com.cps2.edison.lightcontrol.light;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.cps2.edison.lightcontrol.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LightListActivity extends AppCompatActivity implements LightActivityInterface {

    private ListView lightListView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private LightHttpController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_list);
        lightListView = (ListView) findViewById(R.id.lightListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        lightListView.setAdapter(adapter);
        adapter.add("Loading...");
        adapter.notifyDataSetChanged();
        this.controller = new LightHttpController(Volley.newRequestQueue(this), LightListActivity.this);
        this.controller.retrieveLightList();
        lightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String content = listViewItems.get(i);
                String id = content.split(" ")[0];
                Toast.makeText(getApplicationContext(), "Loading light with ID: " + id, Toast.LENGTH_SHORT).show();
                showLight(id);
            }
        });
    }

    public void showLight(String id) {
        Intent intent = new Intent(this, LightControlActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }

    public void onUpdate(String response) {
        try {
            JSONArray lights = new JSONArray(response);
            this.adapter.clear();
            for (int i = 0; i < lights.length(); i++) {
                JSONObject light = lights.getJSONObject(i);
                this.adapter.add(light.getString("id") + " | Level: " + light.getString("level") + " | Status: " + light.getString("status") + " | Room: " + light.getString("roomId"));
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Error loading light list", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.cps2.edison.lightcontrol.building;

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

public class BuildingListActivity extends AppCompatActivity {

    private ListView buildingListView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private BuildingHttpController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);
        buildingListView = (ListView) findViewById(R.id.buildingListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        buildingListView.setAdapter(adapter);
        adapter.add("Loading...");
        adapter.notifyDataSetChanged();
        this.controller = new BuildingHttpController(Volley.newRequestQueue(this), BuildingListActivity.this);
        this.controller.retrieveBuildingList();
    }

    public void onUpdate(String response){
        try {
            JSONArray buildings = new JSONArray(response);
            this.adapter.clear();
            for(int i = 0; i < buildings.length(); i++){
                JSONObject building = buildings.getJSONObject(i);
                this.adapter.add(building.getString("id") + " | Name: " + building.getString("name"));
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, "Error loading building list", Toast.LENGTH_SHORT).show();
        }
    }
}

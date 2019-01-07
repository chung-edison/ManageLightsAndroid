package com.cps2.edison.lightcontrol.room;

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

public class RoomListActivity extends AppCompatActivity {

    private ListView roomListView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private RoomHttpController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        roomListView = (ListView) findViewById(R.id.roomListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        roomListView.setAdapter(adapter);
        adapter.add("Loading...");
        adapter.notifyDataSetChanged();
        this.controller = new RoomHttpController(Volley.newRequestQueue(this), RoomListActivity.this);
        this.controller.retrieveRoomList();
    }

    public void onUpdate(String response){
        try {
            JSONArray rooms = new JSONArray(response);
            this.adapter.clear();
            for(int i = 0; i < rooms.length(); i++){
                JSONObject room = rooms.getJSONObject(i);
                this.adapter.add(room.getString("id") + " | Name: " + room.getString("name") + " | Building: " + room.getString("buildingId"));
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, "Error loading room list", Toast.LENGTH_SHORT).show();
        }
    }
}

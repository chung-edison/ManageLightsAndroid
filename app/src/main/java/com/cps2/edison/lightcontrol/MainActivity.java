package com.cps2.edison.lightcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cps2.edison.lightcontrol.building.BuildingListActivity;
import com.cps2.edison.lightcontrol.hmdtSensor.HmdtSensorListActivity;
import com.cps2.edison.lightcontrol.light.LightListActivity;
import com.cps2.edison.lightcontrol.room.RoomListActivity;
import com.cps2.edison.lightcontrol.tempSensor.TempSensorListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toBuildingList(View view) {
        Intent intent = new Intent(this, BuildingListActivity.class);
        startActivity(intent);
    }

    public void toRoomList(View view) {
        Intent intent = new Intent(this, RoomListActivity.class);
        startActivity(intent);
    }

    public void toLightList(View view) {
        Intent intent = new Intent(this, LightListActivity.class);
        startActivity(intent);
    }

    public void toTempSensorList(View view) {
        Intent intent = new Intent(this, TempSensorListActivity.class);
        startActivity(intent);
    }

    public void toHmdtSensorList(View view) {
        Intent intent = new Intent(this, HmdtSensorListActivity.class);
        startActivity(intent);
    }
}

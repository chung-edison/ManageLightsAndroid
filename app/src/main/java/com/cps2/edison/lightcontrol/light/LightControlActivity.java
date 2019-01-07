package com.cps2.edison.lightcontrol.light;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.cps2.edison.lightcontrol.R;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LightControlActivity extends AppCompatActivity implements LightActivityInterface {

    String id;
    private LightHttpController controller;
    private StompClient mStompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);
        // get light id from the intent
        Intent intent = getIntent();
        this.id = intent.getStringExtra(EXTRA_MESSAGE);
        // instantiate controller + queue
        this.controller = new LightHttpController(Volley.newRequestQueue(this), this);
        this.controller.retrieveLightById(id);
    }

    @Override
    protected void onResume() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://faircorp-app-ce.cleverapps.io/websockets/websocket");
        mStompClient.connect();
        Log.d("websocketStatus", "Connected to websocket");

        mStompClient.topic("/topic/lights/" + id).subscribe(res -> {
            try {
                JSONObject response = new JSONObject(res.getPayload());
                Log.d("response", response.toString());
                this.onUpdate(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.mStompClient.disconnect();
        super.onPause();
    }

    @Override
    public void onUpdate(String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView image = ((ImageView) findViewById(R.id.onOffImage));
                try {
                    JSONObject light = new JSONObject(response);
                    ((TextView) findViewById(R.id.lightIdTextView)).setText(light.get("id").toString());
                    ((TextView) findViewById(R.id.roomIdTextView)).setText(light.get("roomId").toString());
                    ((TextView) findViewById(R.id.lightLevelTextView)).setText(light.get("level").toString());
                    if (light.get("status").equals("ON"))
                        image.setImageResource(R.drawable.ic_bulb_on);
                    else
                        image.setImageResource(R.drawable.ic_bulb_off);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LightControlActivity.this, "Error loading light with ID: " + id, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void switchLight(View view) {
        controller.switchLight(id);
    }
}

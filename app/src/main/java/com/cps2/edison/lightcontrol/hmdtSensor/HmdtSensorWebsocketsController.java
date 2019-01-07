package com.cps2.edison.lightcontrol.hmdtSensor;

import android.util.Log;

import org.json.JSONArray;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class HmdtSensorWebsocketsController {

    private StompClient mStompClient;
    private HmdtSensorListActivity context;

    public HmdtSensorWebsocketsController(HmdtSensorListActivity context) {
        this.context = context;
    }

    public void connect() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://faircorp-app-ce.cleverapps.io/websockets/websocket");
        mStompClient.connect();
        Log.d("websocketStatus", "Connected to websocket");

        mStompClient.topic("/topic/humidity-sensors").subscribe(res -> {
            try {
                JSONArray response = new JSONArray(res.getPayload());
                Log.d("websocketResponse", response.toString());
                context.onUpdate(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void disconnect() {
        mStompClient.disconnect();
        Log.d("websocketStatus", "Disconnected from websocket");
    }
}

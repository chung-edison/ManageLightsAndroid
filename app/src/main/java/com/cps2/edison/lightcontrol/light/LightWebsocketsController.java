package com.cps2.edison.lightcontrol.light;

import android.util.Log;

import com.cps2.edison.lightcontrol.hmdtSensor.HmdtSensorListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class LightWebsocketsController {

    private StompClient mStompClient;
    private LightActivityInterface context;

    public LightWebsocketsController(LightActivityInterface context) {
        this.context = context;
    }

    public void connect() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://faircorp-app-ce.cleverapps.io/websockets/websocket");
        mStompClient.connect();
        Log.d("websocketStatus", "Connected to websocket");

        mStompClient.topic("/topic/lights").subscribe(res -> {
            try {
                JSONArray response = new JSONArray(res.getPayload());
                Log.d("response", response.toString());
                context.onUpdate(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void connect(String id) {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://faircorp-app-ce.cleverapps.io/websockets/websocket");
        mStompClient.connect();
        Log.d("websocketStatus", "Connected to websocket");

        mStompClient.topic("/topic/lights/" + id).subscribe(res -> {
            try {
                JSONObject response = new JSONObject(res.getPayload());
                Log.d("response", response.toString());
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

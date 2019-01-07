package com.cps2.edison.lightcontrol.tempSensor;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class TempSensorHttpController {

    private static final String CONTEXT_SERVER_URL = "http://faircorp-app-ce.cleverapps.io/api/temperature-sensors";
    private RequestQueue queue;
    private TempSensorListActivity context;

    public TempSensorHttpController(RequestQueue queue, TempSensorListActivity context) {
        this.queue = queue;
        this.context = context;
    }

    public void retrieveTempSensorList() {
        String url = CONTEXT_SERVER_URL + "/";

        //get room sensed context
        JsonArrayRequest contextRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("response", response.toString());
                            context.onUpdate(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Some error to access URL : Room may not exists...
                        Log.d("error",CONTEXT_SERVER_URL);
                        Toast.makeText(context, "Error loading temperature sensor list", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(contextRequest);
    }
}

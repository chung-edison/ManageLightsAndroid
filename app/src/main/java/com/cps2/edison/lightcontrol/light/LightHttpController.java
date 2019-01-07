package com.cps2.edison.lightcontrol.light;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class LightHttpController {

    private static final String CONTEXT_SERVER_URL = "http://faircorp-app-ce.cleverapps.io/api/lights";
    private RequestQueue queue;
    private LightActivityInterface context;

    public LightHttpController(RequestQueue queue, LightActivityInterface context) {
        this.queue = queue;
        this.context = context;
    }

    public void retrieveLightList() {
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
                        Log.d("error", CONTEXT_SERVER_URL);
                        Toast.makeText((Context) context, "Error loading light list", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(contextRequest);
    }

    public void retrieveLightById(String id) {
        String url = CONTEXT_SERVER_URL + "/" + id + "/";

        //get room sensed context
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                        Log.d("error", CONTEXT_SERVER_URL);
                        Toast.makeText((Context) context, "Error loading light", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(contextRequest);
    }

    public void switchLight(String id) {
        String url = CONTEXT_SERVER_URL + "/" + id + "/switch";

        //get room sensed context
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                        Log.d("error", CONTEXT_SERVER_URL);
                        Toast.makeText((Context) context, "Error loading light", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(contextRequest);
    }
}

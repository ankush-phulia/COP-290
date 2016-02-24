package com.ribieroboys.moodleplus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ankush on 24-Feb-16.
 */
public class Retreive {

    String url;
    String username;
    String password;
    Bundle infoToPass;
    boolean success;
    JSONObject userJSON;
    Intent intent;
    JSONArray courseJSON;
    ArrayList courseListCodes;
    Context context;
    int attempts;

    public Retreive(String url,
            String username,
            String password,
            Bundle infoToPass,
            boolean success,
            JSONObject userJSON,
            Intent intent,
            JSONArray courseJSON,
            ArrayList courseListCodes,
            Context context,int attempts){
        this.url=url;
        this.username=username;
        this.password=password;
        this.infoToPass=infoToPass;
        this.success=success;
        this.userJSON=userJSON;
        this.intent=intent;
        this.courseJSON=courseJSON;
        this.courseListCodes=courseListCodes;
        this.context=context;
        this.attempts=attempts;
    }

    public void retreive_credentials(){
        // check for validity of credentials
        attempts++;
        String loginURL = url + "/default/login.json?userid=" + username + "&password=" + password;
        StringRequest getReq = new StringRequest(Request.Method.GET,
                loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // receive reply from server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            System.out.println(jsonResponse.has("success"));
                            success = jsonResponse.get("success").toString().equals("true");
                            userJSON = jsonResponse.getJSONObject("user");

                            // put the information to pass
                            infoToPass.putString("firstName", userJSON.getString("first_name"));
                            //System.out.println(userJSON.getString("first_name"));
                            infoToPass.putString("lastName", userJSON.getString("last_name"));
                            infoToPass.putString("entryNo", userJSON.getString("entry_no"));
                            infoToPass.putString("email", userJSON.getString("email"));
                            infoToPass.putInt("id", userJSON.getInt("id"));
                            infoToPass.putBoolean("isStudent", userJSON.getInt("type_") == 0);
                            intent.putExtra("loginResponse", infoToPass);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        // send the request to global request queue
        RequestQ.getInstance().addToRequestQ(getReq);
    }

    public void retreive_courses(){
        //retreive course lists
        String courseURL = url + "/courses/list.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
                courseURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // receive reply from server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            courseJSON = jsonResponse.getJSONArray("courses");

                            // Extract the course list codes
                            courseListCodes = new ArrayList<>();
                            for(int i=0; i<courseJSON.length(); i++) {
                                JSONObject course = (JSONObject) courseJSON.get(i);
                                courseListCodes.add(course.getString("code"));
                                intent.putStringArrayListExtra("courseListCodes", courseListCodes);
                            }
                            intent.putExtra("/courses/list.json", response);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

    public void retreive_notifications(){
        //retrieve notifications
        String notiURL = url + "/default/notifications.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
                notiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        intent.putExtra("/default/notifications.json", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);

        //retreive grades
        String gradesURL = url + "/default/grades.json";
        Req = new StringRequest(Request.Method.GET,
                gradesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        intent.putExtra("/default/grades.json", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

    public void retreive_grades(){
        String gradesURL = url + "/default/grades.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
                gradesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        intent.putExtra("/default/grades.json", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

}



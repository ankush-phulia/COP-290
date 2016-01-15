package com.example.ankush.test;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQ extends Application{

    RequestQueue rq;
    static RequestQ RQinstance;

    @Override
    public void onCreate(){
        super.onCreate();
        RQinstance=this;
    }

    public static synchronized RequestQ getInstance() {
        return RQinstance;
    }

    public RequestQueue getRequestQueue() {
        if (rq == null) {
            rq = Volley.newRequestQueue(getApplicationContext());
        }
        return rq;
    }

    public <T> void addToRequestQ(Request<T> request){
        this.getRequestQueue().add(request);
    }

}
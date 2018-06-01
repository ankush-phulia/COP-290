package com.ribieroboys.moodleplus;

import android.app.Application;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.net.CookieHandler;
import java.net.CookieManager;

public class RequestQ extends Application {

    // create a global request queue to cater to server requests

    RequestQueue rq;
    static RequestQ RQinstance;

    CookieManager cMan;

    @Override
    public void onCreate() {
        super.onCreate();
        RQinstance = this;
        rq = Volley.newRequestQueue(getApplicationContext());
        cMan = new CookieManager();
        CookieHandler.setDefault(cMan);
    }

    public static synchronized RequestQ getInstance() {
        return RQinstance;
    }

    public RequestQueue getRequestQueue() {
        return rq;
    }

    public <T> void addToRequestQ(Request<T> request) {
        // to add any type of request to the queue
        this.getRequestQueue().add(request);
        Log.d("addToRequestQ", "4");
    }
}

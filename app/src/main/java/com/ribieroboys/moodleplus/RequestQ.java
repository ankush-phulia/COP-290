package com.ribieroboys.moodleplus;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQ extends Application {

    //create a global request queue to cater to server requests

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
        //fetches the current request queue or creates a new one till the app lifecycle does not terminate in its entirety
        if (rq == null) {
            rq = Volley.newRequestQueue(getApplicationContext());
        }
        return rq;
    }

    public <T> void addToRequestQ(Request<T> request){
        //to add any type of request to the queue
        this.getRequestQueue().add(request);
    }

}

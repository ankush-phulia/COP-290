package com.ribieroboys.moodleplus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/** Created by Ankush on 24-Feb-16. */
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

    public Retreive(
            String url,
            String username,
            String password,
            Bundle infoToPass,
            boolean success,
            JSONObject userJSON,
            Intent intent,
            JSONArray courseJSON,
            ArrayList courseListCodes,
            Context context,
            int attempts) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.infoToPass = infoToPass;
        this.success = success;
        this.userJSON = userJSON;
        this.intent = intent;
        this.courseJSON = courseJSON;
        this.courseListCodes = courseListCodes;
        this.context = context;
        this.attempts = attempts;
    }
}

package com.ribieroboys.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewThread extends AppCompatActivity implements View.OnClickListener {
    EditText commentToAdd;
    Button postComment;
    TextView comments;
    TextView threadName;
    TextView description;
    String url;
    String threadID;
    JSONObject threadsJSON;
    JSONArray commentsJSON, timesReadableJSON, commentUsersJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_thread);

        getIDs();
        postComment.setOnClickListener(this);

        setText();
    }

    private void setText() {
        try {
            threadName.setText(threadsJSON.getString("title"));
            description.setText(threadsJSON.getString("description"));

            String textToSet = "";
            for (int commentNo = 0; commentNo < commentsJSON.length(); commentNo++) {
                JSONObject temp = (JSONObject) commentUsersJSON.get(commentNo);
                textToSet +=
                        temp.getString("first_name") + " " + temp.getString("last_name") + "\t";
                temp = (JSONObject) commentsJSON.get(commentNo);
                textToSet += temp.getString("description") + "\n";

                textToSet += "\t" + timesReadableJSON.get(commentNo);
                textToSet += "\n\n";
            }
            comments.setText(textToSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIDs() {
        commentToAdd = (EditText) findViewById(R.id.commentToAdd);
        postComment = (Button) findViewById(R.id.add_comment);
        comments = (TextView) findViewById(R.id.comments_section);
        threadName = (TextView) findViewById(R.id.thread_name);
        description = (TextView) findViewById(R.id.description_thread);

        Intent prev = getIntent();
        url = prev.getStringExtra("URL");
        threadID = prev.getStringExtra("threadID");
        try {
            threadsJSON = new JSONObject(prev.getStringExtra("threads"));
            commentsJSON = new JSONArray(prev.getStringExtra("comments"));
            timesReadableJSON = new JSONArray(prev.getStringExtra("timesReadable"));
            commentUsersJSON = new JSONArray(prev.getStringExtra("commentUsers"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        if (view == postComment) {
            String commentURL =
                    url
                            + "/threads/post_comment.json?thread_id="
                            + threadID
                            + "&description="
                            + commentToAdd.getText();
            StringRequest getReq =
                    new StringRequest(
                            Request.Method.GET,
                            commentURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // receive reply from server
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        System.out.println(
                                                jsonResponse
                                                        .get("success")
                                                        .toString()
                                                        .compareTo("true"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(
                                                    ViewThread.this,
                                                    "Connection Error",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

            // send the request to RequestQ
            RequestQ.getInstance().addToRequestQ(getReq);

            Intent backToMain = new Intent(getIntent());
            backToMain.setClass(ViewThread.this, Main.class);
            backToMain.putExtra("fromNewThread", true);
            startActivity(backToMain);
            this.finish();
        }
    }
}

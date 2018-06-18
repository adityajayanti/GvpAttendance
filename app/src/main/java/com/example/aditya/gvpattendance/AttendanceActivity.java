package com.example.aditya.gvpattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertStoreException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {
    DataTimeHandler dataTimeHandler;
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    TextView textdept , textsem , textsub;
    String dept , sem , sub , hr;
    String jsonReply;
    Handler handler;
    EditText topicName;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        jsonReply ="";
        textdept = (TextView)findViewById(R.id.TextDept);
        textsem = (TextView)findViewById(R.id.TextSem);
        textsub = (TextView)findViewById(R.id.TextSub);
        topicName=(EditText)findViewById(R.id.TopicName);
        topicName.setText("");
        Intent intent =getIntent();
        textdept.setText(intent.getStringExtra("dept").toString());
        textsem.setText(intent.getStringExtra("sem").toString());
        textsub.setText(intent.getStringExtra("sub").toString());
        Toast.makeText(getApplicationContext(),intent.getStringExtra("position").toString(),Toast.LENGTH_SHORT).show();

        dept = intent.getStringExtra("dept").toString();
        sem = intent.getStringExtra("sem").toString();
        sub = intent.getStringExtra("sub").toString();
        hr = intent.getStringExtra("hour").toString();

        button = (Button) findViewById(R.id.button);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                contentsAdapter = new ContentsAdapter(AttendanceActivity.this, contentsList, 3);
                recyclerView.setAdapter(contentsAdapter);
                insertAtt();
            }
        });

        //adding some items to our list
        loadProducts();

    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                Log.i("String " , line);
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("Return" , sb.toString());
        return sb.toString();

    }

    public void toast(final Context context, final String text) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AttendanceActivity.this,AttendanceMenu.class);
                startActivity(intent);
            }
        });
    }

    private void insertAtt(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    //constants
                    URL url = new URL(Constansts.InsertAttendanceURL);
                    JSONObject jGroup ;
                    JSONArray jResult = new JSONArray();
                    for (int i = 0; i < contentsList.size(); i++) {
                        jGroup = new JSONObject();
                        try {
                            jGroup.put("SID", contentsList.get(i).getStu_id().toString());
                            jGroup.put("AttDate", dataTimeHandler.getTodayDate().toString());
                            jGroup.put("FID", SharedPrefManager.getInstance(AttendanceActivity.this).getFid().toString());
                            jGroup.put("CNAME", sub);
                            jGroup.put("CDEPT", dept);
                            jGroup.put("CSEM" ,sem );
                            jGroup.put("CHOUR", hr);
                            jGroup.put("TopicName",topicName.getText().toString());
                            jGroup.put("ATTS",contentsList.get(i).get_PA());

                            jResult.put(jGroup);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    String message = jResult.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout( 10000 /*milliseconds*/ );
                    conn.setConnectTimeout( 15000 /* milliseconds */ );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();


                    if(conn.getResponseCode()==201 || conn.getResponseCode()==200) {

                        //do somehting with response
                        is = conn.getInputStream();
                        jsonReply = convertStreamToString(is);
                        toast(AttendanceActivity.this,jsonReply);
                        //String contentAsString = readIt(is,len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //clean up
                    try {
                        os.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();
                }
            }
        }).start();
    }


    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.StudentAttendanceURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);

                                String SID = object.getString("sid");
                                String SNAME = object.getString("sname");

                                MainPageContents mainPageContents = new MainPageContents(SID,SNAME,false);
                                contentsList.add(mainPageContents);

                            }

                            contentsAdapter = new ContentsAdapter(AttendanceActivity.this, contentsList, 3);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Toast.makeText(getApplicationContext()," ",Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Stu_dept", dept);
                params.put("Stu_sem", sem);
                params.put("Stu_sub", sub);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.features_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.Logout:
                SharedPrefManager.getInstance(this).islogout();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.backto:
                startActivity(new Intent(getApplicationContext(), AttendanceMenu.class));
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();



        }
        return  true;

    }
}

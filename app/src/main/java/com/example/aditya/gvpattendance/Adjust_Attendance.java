package com.example.aditya.gvpattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adjust_Attendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DataTimeHandler dataTimeHandler;
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dept , sem,hr,sub;
    String jsonReply;
    Handler handler;
    private JSONArray result;
    EditText topicName;
    ArrayList<String> subjects = new ArrayList<String>();
    private Button button;
    Spinner spinner4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust__attendance);

        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        jsonReply="";
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        Spinner spinner3 = findViewById(R.id.spinner3);
        topicName = (EditText)findViewById(R.id.TopicName);
        spinner4 = findViewById(R.id.spinner4);
        button = (Button) findViewById(R.id.button);

        loadData();
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.dept ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter1 = ArrayAdapter.createFromResource(this,R.array.sem ,android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter1);
        spinner2.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this,R.array.hour ,android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(arrayAdapter2);
        spinner3.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(this,R.array.subject ,android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(arrayAdapter3);
        spinner4.setOnItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if((dept.equals("Dept") || sem.equals("Sem")) || hr.equals("Hour")|| sub.equals("Subject")||((dept.equals("MSC") && sem.equals("5") )) ||((dept.equals("MSC") && sem.equals("6") ) )) {
                    Toast.makeText(getApplicationContext(),"Something is wrong!",Toast.LENGTH_SHORT).show();

                }else{
                    contentsAdapter = new ContentsAdapter(Adjust_Attendance.this, contentsList, 3);
                    recyclerView.setAdapter(contentsAdapter);
                    insertAtt();

                }
            }
        });

    }

    public void getStudents(JSONArray json ){
        for(int i=0;i<json.length();i++){
            JSONObject e = null;
            try {
                e = json.getJSONObject(i);
                if(!e.getString("cname").equals("CTV")){
                    subjects.add(e.getString("cname"));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        spinner4.setAdapter(new ArrayAdapter<String>(Adjust_Attendance.this, android.R.layout.simple_spinner_dropdown_item, subjects));

    }

    private void loadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.MySubjectURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result = new JSONArray(response);
                            getStudents(result);


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
                params.put("fid", SharedPrefManager.getInstance(Adjust_Attendance.this).getFid().toString());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

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
                Intent intent = new Intent(Adjust_Attendance.this,AttendanceMenu.class);
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
                            jGroup.put("FID", SharedPrefManager.getInstance(Adjust_Attendance.this).getFid().toString());
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
                        toast(Adjust_Attendance.this,jsonReply);
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
                            contentsList.clear();
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);

                                String SID = object.getString("sid");
                                String SNAME = object.getString("sname");

                                MainPageContents mainPageContents = new MainPageContents(SID,SNAME,false);
                                contentsList.add(mainPageContents);

                            }

                            contentsAdapter = new ContentsAdapter(Adjust_Attendance.this, contentsList, 3);
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
        getMenuInflater().inflate(R.menu.adjust_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.backto:
                startActivity(new Intent(getApplicationContext(), AttendanceMenu.class));
                break;

            case R.id.searchRecord:
                loadProducts();
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinner1)
        {
            dept = parent.getItemAtPosition(position).toString();
        }
        else if(spinner.getId() == R.id.spinner2)
        {
            sem = parent.getItemAtPosition(position).toString();
        }
        else if(spinner.getId() == R.id.spinner3)
        {
            hr = parent.getItemAtPosition(position).toString();
        }
        else if(spinner.getId() == R.id.spinner4)
        {
            sub = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

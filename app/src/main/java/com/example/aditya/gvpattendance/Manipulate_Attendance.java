package com.example.aditya.gvpattendance;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Manipulate_Attendance extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    EditText edittext_DateSearch;
    Button Sbtn, upBtn;
    Calendar myCalendar;
    String dept , sem , hr ;
    String ManiSelect;
    Handler handler;
    DataTimeHandler dataTimeHandler;
    String jsonReply;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulate__attendance);

        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        myCalendar = Calendar.getInstance();

        Intent intent =getIntent();
        ManiSelect = intent.getStringExtra("ManiSelect").toString();

        jsonReply ="";
        edittext_DateSearch= (EditText) findViewById(R.id.DateSearch);
        Sbtn = (Button) findViewById(R.id.Sbtn);
        upBtn = (Button) findViewById(R.id.button);
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        Spinner spinner3 = findViewById(R.id.spinner3);
        if(ManiSelect.equals("1")){
            upBtn.setText("Update");
        }else if(ManiSelect.equals("2")){
            upBtn.setText("Delete");
        }

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

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        Sbtn.setOnClickListener(this);
        upBtn.setOnClickListener(this);
        edittext_DateSearch.setOnClickListener(this);
    }

    public void GetStudentInfo(){
        if((dept.equals("Dept") || sem.equals("Sem")) || (((dept.equals("MSC") && sem.equals("5") )) ||((dept.equals("MSC") && sem.equals("6") ) ))){
        }
        else {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            contentsList = new ArrayList<>();
            loadProducts();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {

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
        return sb.toString();

    }

    public void toast(final Context context, final String text) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Manipulate_Attendance.this,AttendanceMenu.class);
                startActivity(intent);
            }
        });
    }

    private void deleteAtt(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    //constants
                    URL url = new URL(Constansts.DeleteAttendanceURL);
                    JSONObject jGroup ;
                    JSONArray jResult = new JSONArray();
                    for (int i = 0; i < contentsList.size(); i++) {
                        jGroup = new JSONObject();
                        try {
                            jGroup.put("AttDate", edittext_DateSearch.getText().toString());
                            jGroup.put("CDEPT", dept);
                            jGroup.put("CSEM", sem);
                            jGroup.put("CHOUR", hr);
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
                        toast(Manipulate_Attendance.this,jsonReply);
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

    private void updateAtt(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    //constants
                    URL url = new URL(Constansts.UpdateAttendanceURL);
                    JSONObject jGroup ;
                    JSONArray jResult = new JSONArray();
                    for (int i = 0; i < contentsList.size(); i++) {
                        jGroup = new JSONObject();
                        try {
                            jGroup.put("SID", contentsList.get(i).getStu_id().toString());
                            jGroup.put("AttDate", edittext_DateSearch.getText().toString());
                            jGroup.put("CDEPT", dept);
                            jGroup.put("CSEM", sem);
                            jGroup.put("CHOUR", hr);
                            jGroup.put("ATTS", contentsList.get(i).get_PA());
                            jResult.put(jGroup);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    String message = jResult.toString();
                    Log.i("Json",message);
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
                        toast(Manipulate_Attendance.this,jsonReply);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.ManipulateAttendanceURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);
                                Boolean PA;
                                String SID = object.getString("sid");
                                String SNAME = object.getString("sname");
                                String SStatus = object.getString("ss");
                                if(SStatus.equals("1")){
                                     PA = true;
                                }else{
                                     PA = false;
                                }
                                MainPageContents mainPageContents = new MainPageContents(SID,SNAME,PA);
                                contentsList.add(mainPageContents);

                            }
                            contentsAdapter = new ContentsAdapter(Manipulate_Attendance.this, contentsList, 3);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {


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
                params.put("dates", edittext_DateSearch.getText().toString());
                params.put("dept",dept);
                params.put("sem",sem);
                params.put("hour",hr);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-d"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            edittext_DateSearch.setText(sdf.format(myCalendar.getTime()));
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

        }else if(spinner.getId() == R.id.spinner3){

            hr=parent.getItemAtPosition(position).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.features_menu,menu);
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


    @Override
    public void onClick(View v) {
        if(v == edittext_DateSearch) {
            new DatePickerDialog(Manipulate_Attendance.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if(v == Sbtn){
            GetStudentInfo();
        }else if(v == upBtn){
            if(ManiSelect.equals("1")) {
                contentsAdapter = new ContentsAdapter(Manipulate_Attendance.this, contentsList, 3);
                recyclerView.setAdapter(contentsAdapter);
                updateAtt();
            }else if(ManiSelect.equals("2")){
                deleteAtt();
            }
        }
    }
}

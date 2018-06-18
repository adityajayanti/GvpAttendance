package com.example.aditya.gvpattendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendancePercent extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dept , sem;
    String dateFrom , dateTo;
    Calendar myCalendar;
    int anInt = 0;
    DatePickerDialog.OnDateSetListener date;
    Button Sbtn;

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

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.OverallAttendanceURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);

                                String SID = object.getString("sid");
                                String SNAME = object.getString("sname");
                                String SAttended = object.getString("presentCount");
                                String SHelded = object.getString("totalCount");
                                String SPer = object.getString("percentAtt");
                                MainPageContents mainPageContents = new MainPageContents(SID,SNAME,SAttended,SHelded,SPer);
                                contentsList.add(mainPageContents);

                            }
                            contentsAdapter = new ContentsAdapter(AttendancePercent.this, contentsList, 4);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Toast.makeText(getApplicationContext(),"More Student Information",Toast.LENGTH_SHORT).show();

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
                params.put("dept",dept);
                params.put("sem",sem);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_percent);

        DataTimeHandler dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        myCalendar = Calendar.getInstance();
        Sbtn = (Button) findViewById(R.id.Sbtn);
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.dept ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter1 = ArrayAdapter.createFromResource(this,R.array.sem ,android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter1);
        spinner2.setOnItemSelectedListener(this);

        Sbtn.setOnClickListener(this);


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
                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                finish();
                break;
            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }

    @Override
    public void onClick(View v) {
       if(v == Sbtn){
            GetStudentInfo();
        }
    }
}

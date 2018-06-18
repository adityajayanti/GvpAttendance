package com.example.aditya.gvpattendance;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student_info extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dept ,sem;


    public void GetStudentInfo(View view){
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
       StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.StudentInfoURL,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONArray timetablelist = new JSONArray(response);
                           for (int i = 0; i < timetablelist.length(); i++) {
                               JSONObject object = timetablelist.getJSONObject(i);

                               String SID = object.getString("sid");
                               String SNAME = object.getString("sname");
                               String SDEPT = object.getString("sdept");
                               int SSEM = Integer.valueOf(object.getString("ssem"));
                               long SMOB = Long.valueOf(object.getString("smob"));
                               String SEMAIL = object.getString("semail");

                               MainPageContents mainPageContents = new MainPageContents(SID,SNAME,SDEPT,SSEM,SMOB,SEMAIL);
                               contentsList.add(mainPageContents);

                           }
                           contentsAdapter = new ContentsAdapter(Student_info.this, contentsList, 2);
                           //setting adapter to recyclerview
                           recyclerView.setAdapter(contentsAdapter);
                           contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                               @Override
                               public void onItemClick(int position) {
                                   /*Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                                   intent.putExtra("dept", contentsList.get(position).getTitle());
                                   intent.putExtra("sem", String.valueOf(contentsList.get(position).getSem()));
                                   intent.putExtra("sub", contentsList.get(position).getTimehour());
                                   startActivity(intent);
                                   finish();*/
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
               params.put("Stu_dept", dept);
               params.put("Stu_sem", sem);
               return params;
           }
       };

       RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        getSupportActionBar().setTitle("Students Information");

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
}

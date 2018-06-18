package com.example.aditya.gvpattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MySubject extends AppCompatActivity {
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    DataTimeHandler dataTimeHandler;
    String value ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subject);
        value = "value";
        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());
        Intent intent =getIntent();
        value = intent.getStringExtra("value");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();
        if(value.equals("one")){
            CTVproducts();
        }if(value.equals("two")) {
            loadProducts();
        }
    }

    private void CTVproducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.CTVURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);
                                String DEPT = object.getString("dept");
                                String SEM = object.getString("sem");
                                MainPageContents mainPageContents = new MainPageContents("CTV",DEPT,SEM);
                                contentsList.add(mainPageContents);

                            }
                            contentsAdapter = new ContentsAdapter(MySubject.this, contentsList, 5);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                    Intent intent = new Intent(getApplicationContext(),CtvAttendance.class);
                                    intent.putExtra("sub",contentsList.get(position).getTitle());
                                    intent.putExtra("dept",contentsList.get(position).getDesc());
                                    intent.putExtra("sem",contentsList.get(position).getTimehour());
                                    startActivity(intent);
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
                params.put("fid", SharedPrefManager.getInstance(MySubject.this).getFid().toString());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.MySubjectURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for (int i = 0; i < timetablelist.length(); i++) {
                                JSONObject object = timetablelist.getJSONObject(i);
                                String SUB = object.getString("cname");
                                String DEPT = object.getString("dept");
                                String SEM = object.getString("sem");
                                MainPageContents mainPageContents = new MainPageContents(SUB,DEPT,SEM);
                                contentsList.add(mainPageContents);

                            }
                            contentsAdapter = new ContentsAdapter(MySubject.this, contentsList, 5);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(getApplicationContext(),MySubAttendance.class);
                                    intent.putExtra("sub",contentsList.get(position).getTitle());
                                    intent.putExtra("dept",contentsList.get(position).getDesc());
                                    intent.putExtra("sem",contentsList.get(position).getTimehour());
                                    startActivity(intent);
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
                params.put("fid", SharedPrefManager.getInstance(MySubject.this).getFid().toString());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
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
                startActivity(new Intent(getApplicationContext(),AttendanceMenu.class));
                finish();

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }
}

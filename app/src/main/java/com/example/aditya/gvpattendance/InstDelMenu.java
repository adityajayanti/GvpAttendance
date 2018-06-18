package com.example.aditya.gvpattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class InstDelMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    DataTimeHandler dataTimeHandler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_del_menu);

        Intent intent = getIntent();
        final String op = intent.getStringExtra("op");
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contentsList = new ArrayList<>();
        //adding some items to our list
        contentsList.add(
                new MainPageContents(
                        "Subject information",
                        "Click to " +op+ " subject information.",
                        R.drawable.red));

        contentsList.add(
                new MainPageContents(
                        "CTV information",
                        "Click to " +op+ " CTV information.",
                        R.drawable.green));

        contentsList.add(
                new MainPageContents(
                        "Elective subjects",
                        "Click to " +op+ " elective subject information.",
                        R.drawable.red));

        contentsList.add(
                new MainPageContents(
                        "Timetable",
                        "Click to " +op+ " timetable information.",
                        R.drawable.green));

        contentsList.add(
                new MainPageContents(
                        "faculty information",
                        "Click to " +op+ " faculty information.",
                        R.drawable.red));

        contentsList.add(
                new MainPageContents(
                        "Lab information",
                        "Click to " +op+ " lab information.",
                        R.drawable.green));

        contentsList.add(
                new MainPageContents(
                        "Student attendance information",
                        "Click to " +op+ " student attendance information.",
                        R.drawable.red));

        contentsList.add(
                new MainPageContents(
                        "Student elective",
                        "Click to " +op+ " student elective information.",
                        R.drawable.green));

        contentsList.add(
                new MainPageContents(
                        "Student information",
                        "Click to " +op+ " student information.",
                        R.drawable.red));

        contentsList.add(
                new MainPageContents(
                        "Student semester",
                        "Click to " +op+ " student semester information.",
                        R.drawable.green));


        contentsAdapter = new ContentsAdapter(this, contentsList,0);

        //setting adapter to recyclerview
        recyclerView.setAdapter(contentsAdapter);

        contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(op.equals("insert")) {
                    if (position == 0) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","subInfo");
                        startActivity(intent);
                        finish();

                    } else if (position == 1) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","ctvInfo");
                        startActivity(intent);
                        finish();
                    } else if (position == 2) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","eleSubInfo");
                        startActivity(intent);
                        finish();

                    } else if (position == 3) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","timetableInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 4) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","facultyInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 5) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","labInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 6) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","stuAttInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 7) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","stuEleInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 8) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","studentInfo");
                        startActivity(intent);
                        finish();
                    }else if (position == 9) {
                        Intent intent = new Intent(getApplicationContext(), AdminFileUpdate.class);
                        intent.putExtra("pos", contentsList.get(position).getTitle());
                        intent.putExtra("name","stuSemInfo");
                        startActivity(intent);
                        finish();
                    }
                }else if (op.equals("delete")){
                    String dbtable ="";
                    switch(position){
                        case 0: dbtable=contentsList.get(position).getTitle();
                              break;
                        case 1:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 2:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 3:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 4:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 5:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 6:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 7:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 8:dbtable=contentsList.get(position).getTitle();
                            break;
                        case 9:dbtable=contentsList.get(position).getTitle();
                            break;

                    }
                    loadData(dbtable);
                }
            }
        });


    }
    private void loadData(final String dbtable) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.DelDbURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
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
                params.put("dbname", dbtable);
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
                startActivity(new Intent(getApplicationContext(), InsertDeleteDbMenu.class));
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();


        }
        return  true;

    }
}

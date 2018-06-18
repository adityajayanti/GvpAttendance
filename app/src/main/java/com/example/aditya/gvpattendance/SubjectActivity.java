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

public class SubjectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dayname ,fid ,aNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        getSupportActionBar().setTitle(SharedPrefManager.getInstance(this).getUsername());
        fid = SharedPrefManager.getInstance(this).getFid();

        Intent intent =getIntent();
        aNO = intent.getStringExtra("activityno").toString();
        dayname= intent.getStringExtra("weekname").toString();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();
        //adding some items to our list
        loadProducts();

    }
        private void loadProducts(){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.TimeTableURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray timetablelist = new JSONArray(response);
                                for(int i = 0 ; i<timetablelist.length();i++){
                                    JSONObject object = timetablelist.getJSONObject(i);

                                    String dept = object.getString("dept");
                                    int id = object.getInt("hour");
                                    String idString = String.valueOf(id);
                                    int sem = object.getInt("sem");
                                    String course_name = object.getString("course_name");

                                    MainPageContents mainPageContents = new MainPageContents(dept , idString , course_name,sem);
                                    contentsList.add(mainPageContents);

                                }

                                contentsAdapter = new ContentsAdapter(SubjectActivity.this, contentsList,1);
                                //setting adapter to recyclerview
                                recyclerView.setAdapter(contentsAdapter);
                                contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        if(aNO.equals("Week")){
                                            Toast.makeText(getApplicationContext(),contentsList.get(position).getTimehour(),Toast.LENGTH_SHORT).show();
                                        }else if(contentsList.get(position).getTitle().toString().equals("--")) {
                                            Toast.makeText(getApplicationContext(),"NO ATTENDANCE",Toast.LENGTH_SHORT).show();

                                        }else{
                                            Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                                            intent.putExtra("dept", contentsList.get(position).getTitle());
                                            intent.putExtra("sem", String.valueOf(contentsList.get(position).getSem()));
                                            intent.putExtra("sub", contentsList.get(position).getTimehour());
                                            intent.putExtra("hour", contentsList.get(position).getDesc());
                                            intent.putExtra("position",String.valueOf(position));
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Occured" , Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String , String > params = new HashMap<>();
                    params.put("f_college_id",fid);
                    params.put("day_name", dayname);
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
                if(aNO.equals("Week")) {
                    startActivity(new Intent(getApplicationContext(), WeekActivity.class));
                } else if (aNO.equals("Main2")) {
                    startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                }
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }
}

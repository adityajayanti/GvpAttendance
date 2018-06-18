package com.example.aditya.gvpattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    DataTimeHandler dataTimeHandler;
    public void PageOne(View view){

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());
        Toast.makeText(getApplicationContext(),"Welcome "+SharedPrefManager.getInstance(this).getUsername(),Toast.LENGTH_LONG).show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contentsList = new ArrayList<>();
        //adding some items to our list
        contentsList.add(
                new MainPageContents(
                        "TIMETABLE",
                        "Get to know the classes, only a click away.",
                        R.drawable.timetable));

        contentsList.add(
                new MainPageContents(
                        "ATTENDANCE",
                        "Manage attendance in just few clicks.",
                        R.drawable.attendance));

        contentsList.add(
                new MainPageContents(
                        "STUDENTS",
                        "Get student information, now student's can't get away.",
                        R.drawable.student));

        contentsList.add(
                new MainPageContents(
                        "SETTINGS",
                        "Manage your profile.",
                        R.drawable.setting));


        contentsAdapter = new ContentsAdapter(this, contentsList,0);

        //setting adapter to recyclerview
        recyclerView.setAdapter(contentsAdapter);

        contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == 0) {
                    Toast.makeText(getApplicationContext(), contentsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), WeekActivity.class));
                }
                else if(position == 1){
                    Intent intent = new Intent(getApplicationContext(),AttendanceMenu.class);
                    intent.putExtra("weekname",dataTimeHandler.getWeekDay().toString());
                    intent.putExtra("activityno","Main2");
                    startActivity(intent);
                    finish();
                }
                else if(position ==2){
                    Intent intent = new Intent(getApplicationContext(),Student_info.class);
                    startActivity(intent);
                    finish();

                }else if(position ==3) {
                    Intent intent = new Intent(getApplicationContext(), SettingMenu.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
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

        }
        return  true;
    }
}

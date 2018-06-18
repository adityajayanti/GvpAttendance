package com.example.aditya.gvpattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    DataTimeHandler dataTimeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contentsList = new ArrayList<>();
        //adding some items to our list
        contentsList.add(
                new MainPageContents(
                        "Monday",
                        "Good Start means Good End",
                        R.drawable.mon));

        contentsList.add(
                new MainPageContents(
                        "Tuesday",
                        "The breif syllabus of every subject.",
                        R.drawable.tue));

        contentsList.add(
                new MainPageContents(
                        "Wednesday",
                        "Manage attendance in just few clicks.",
                        R.drawable.wed));

        contentsList.add(
                new MainPageContents(
                        "Thusrday",
                        "Get student information, now student's can't get away.",
                        R.drawable.thu));

        contentsList.add(
                new MainPageContents(
                        "Friday",
                        "Manage your profile.",
                        R.drawable.fri));

        contentsList.add(
                new MainPageContents(
                        "Saturday",
                        "Manage your profile.",
                        R.drawable.sat));



        contentsAdapter = new ContentsAdapter(this, contentsList,0);

        //setting adapter to recyclerview
        recyclerView.setAdapter(contentsAdapter);
        contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(getApplicationContext(),contentsList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),SubjectActivity.class);
                intent.putExtra("weekname",contentsList.get(position).getTitle().substring(0,3));
                intent.putExtra("activityno","Week");
                startActivity(intent);
                finish();
            }
        });


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
                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                finish();

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }
}

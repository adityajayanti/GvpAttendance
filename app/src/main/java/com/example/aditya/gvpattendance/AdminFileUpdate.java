package com.example.aditya.gvpattendance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminFileUpdate extends AppCompatActivity{

    private Button button;
    private Button button2;
    private TextView T1 , T2 , T3 ;
    private Handler handler;
    private File f;
    private String content_type;
    private String file_path;
    DataTimeHandler dataTimeHandler;
    private String pos,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_file_update);

        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        Intent intent = getIntent();

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.selectfile);
        T1 = (TextView) findViewById(R.id.SelectionId);
        T2 = (TextView) findViewById(R.id.FileID);
        T3 = (TextView) findViewById(R.id.FileValue);
        T1.setText(intent.getStringExtra("pos"));
        T3.setText("Select File");
        pos = intent.getStringExtra("pos");
        name = intent.getStringExtra("name");
        Log.i("name","Hi "+name+"Hi "+pos);
        Log.i("Url",Constansts.FileURL+name+".php");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        enable_button();

    }

    private void enable_button() {

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(AdminFileUpdate.this)
                        .withRequestCode(10)
                        .start();
            }
        });

    }

    private void enable_button1(final Intent data){

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        content_type = getMimeType(f.getPath());
                        file_path = f.getAbsolutePath();
                        toast1(AdminFileUpdate.this, file_path.substring(file_path.lastIndexOf("/") + 1));
                        OkHttpClient client = new OkHttpClient();
                        try {
                            RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);
                            RequestBody request_body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("type", content_type)
                                    .addFormDataPart(pos, file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                    .build();

                            Request request = new Request.Builder()
                                    .url(Constansts.FileURL+name+".php")
                                    .post(request_body)
                                    .build();


                            try {
                                Response response = client.newCall(request).execute();
                                String jsonData = response.body().string();
                                JSONObject Jobject = new JSONObject(jsonData);
                                Boolean result = Jobject.getBoolean("error");
                                if (!result) {
                                    toast(AdminFileUpdate.this, Jobject.getString("msg"));
                                } else {
                                    toast(AdminFileUpdate.this, Jobject.getString("msg"));
                                }
                                //progress.dism

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            toast(AdminFileUpdate.this, "Can't read this file");
                        }

                    }

                });

                t.start();

            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

   ProgressDialog progress;

    public void toast(final Context context, final String text) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(AdminFileUpdate.this,AttendanceMenu.class);
                //startActivity(intent);
            }
        });
    }

    public void toast1(final Context context, final String text) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                T3.setText(text);
            }
        });
    }



        @Override
        protected void onActivityResult ( int requestCode, int resultCode, final Intent data){
            if (requestCode == 10 && resultCode == RESULT_OK) {

                progress = new ProgressDialog(AdminFileUpdate.this);
                progress.setTitle("Uploading");
                progress.setMessage("Please wait...");
                //progress.show();
                enable_button1(data);
            }else {
                toast(AdminFileUpdate.this, "fail");
            }
        }

    private String getMimeType(String path) {
        String extension = "";
        extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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

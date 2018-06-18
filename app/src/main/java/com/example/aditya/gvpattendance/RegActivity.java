package com.example.aditya.gvpattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    EditText username, pass , cnfpass;
    ProgressDialog progressDialog;
    String user , passwordUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        getSupportActionBar().setTitle("Registration");

        btn = (Button)findViewById(R.id.RegBtn);
        username = (EditText) findViewById(R.id.editUser);
        pass = (EditText) findViewById(R.id.editPass);
        cnfpass = (EditText) findViewById(R.id.editCnfPass);

        progressDialog = new ProgressDialog(this);
        btn.setOnClickListener(this);
    }

    public void reg(){
        user = username.getText().toString().trim();
        passwordUser = pass.getText().toString().trim() ;

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.RegURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                            intent.putExtra("username" ,jsonObject.getString("username") );
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user_name",user);
                params.put("user_pass",passwordUser);
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
    @Override
    public void onClick(View v) {
        if(v == btn){
            if(pass.getText().toString().equals(cnfpass.getText().toString())){
                reg();
            }
            else{
                Toast.makeText(getApplicationContext(),"Password Mis-match",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
